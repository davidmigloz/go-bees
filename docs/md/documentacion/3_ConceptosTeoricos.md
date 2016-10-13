Conceptos teóricos
==================

{En aquellos proyectos que necesiten para su comprensión y desarrollo de unos conceptos teóricos de una determinada materia o de un determinado dominio de conocimiento, debe existir un apartado que sintetice dichos conceptos.}

Visión artificial
-----------------

### 1º Preprocesado

Antes de aplicar los substracción del fondo, es recomendable realizar un preprocesado de los fotogramas para facilitar el procesado, minimizar el ruido y optimizar los resultados. En primer lugar, se convertirán los fotogramas a escala de grises. Y en segundo lugar, aplicaremos un desenfoque gausiano para minimizar el ruido provocado por la propia cámara y por la iluminación.

#### Conversión de RGB a escala de grises
asdf

#### Desenfoque gausiano
asdf


### 2º Substracción del fondo

En un sistema de vigilancia resulta de gran interés el poder extraer los objetos en movimiento. Esto se conoce como extracción del fondo, en inglés _background subtraction_ o _foreground detection_. Entendemos por esto la clasificación de todos los píxeles de un determinado fotograma bien como fondo, o como primer plano. [^wiki:bs] En primer plano se engloban a todos los objetos en movimiento, mientras que en el fondo se encuentran todos los objetos estáticos junto con posibles sombras, cambios de iluminación u otros objetos en movimiento que no son de interés, como puede ser la rama de un árbol balanceándose por el viento. Se trata de un paso crítico, ya que algoritmos posteriores (detección y tracking) dependen en gran medida de los resultados de este.

En nuestro proyecto, la toma de imágenes se realiza mediante una cámara estática. Esto facilita en parte la detección del fondo, ya que este se corresponderá con todos los píxeles estáticos. Sin embargo, como trabajamos en un entorno al aire libre, tenemos que lidiar también con cambios de iluminación, sombras, u otros objetos móviles que no son de nuestro interés (falsos positivos).

Con OpenCV para Android podemos implementar varios algoritmos básicos de extracción del fondo que comentaremos a continuación. Además, nos proporciona la implementación de dos algoritmos más sofisticados: `BackgroundSubtractorMOG` y `BackgroundSubtractorKNN`.

#### Substracción con imagen de referencia
Se parte de una imagen de referencia del fondo, en la que no haya ningún objeto en movimiento. A partir de esta, se obtienen los elementos en movimiento substrayendo a cada fotograma la imagen tomada como referencia.

Este método, al tomar un modelo del fondo tan sencillo y estático, es muy vulnerable a cambios en la escena (iluminación, sombras, objetos del fondo con ligeros movimientos, pequeñas oscilaciones de la cámara, etc). Sin embargo, ofrece muy buenos resultados cuando se trabaja en una escena con la iluminación y los elementos controlados, ya que al ser tan simple, es muy eficiente. [^programarfacil:detmov]

Para implementar este algoritmo con OpenCV, se hace uso de la función `Core.absdiff()`.

En  nuestro problema, al trabajar al aire libre nos es imposible utilizar este algoritmo. Ya que el modelo del fondo cambia constantemente.

#### Substracción del fotograma anterior
En este método el modelo del fondo se extrae del fotograma anterior. De tal manera que a cada nuevo fotograma se le substrae el anterior.

De esta manera se mejora la respuesta a cambios en la escena, como los cambios de iluminación. Sin embargo, si un objeto en movimiento se queda estático en la imagen, este deja de ser detectado. [^book:opencv_java]

La implementación se realiza como en la técnica anterior, variando el modelo del fondo.

Tras probarlo en nuestro problema específico, vimos que no nos era de utilidad porque detectaba las abejas por duplicado (una por cada fotograma).

#### Substracción de los fotogramas anteriores
Una mejora interesante del algoritmo anterior supone tomar como modelo del fondo un acumulado de los fotogramas anteriores de acuerdo a un ratio de aprendizaje. De esta forma, se puede lidiar con cambios en el fondo de la imagen dinámicamente. El modelo se calcula de acuerdo a la siguiente fórmula:

![](https://latex.codecogs.com/gif.latex?%5CLARGE%20u_t%20%3D%20%281-%5Calpha%20%29u_%7Bt-1%7D&plus;%5Calpha%5C%20p_t)

`u_t = (1-\alpha )u_{t-1}+\alpha\ p_t`

Donde  p_t es el nuevo valor del píxel, u_{t-1} es la media del fondo en el instante t-1, u_t es la nueva media del fondo y \alpha es el ratio de aprendizaje (cómo de rápido olvida los frames anteriores). [^book:opencv_java]

OpenCV provee la función `Imgproc.accumulateWeighted()` que implementa por nosotros la fórmula anterior. Haciendo uso de esta función y de la utilizada en la sección anterior podemos implementar este algoritmo.

Tras probarlo vimos que tenía una eficiencia muy buena y se adaptaba a los cambios correctamente. Sin embargo, de vez en cuando se producían ruidos que daban lugar a falsos positivos.

#### BackgroundSubtractorMOG2

`BackgroundSubtractorMOG2` es una mejora del algoritmo `BackgroundSubtractorMOG`. En la versión original de OpenCV se encuentran implementados ambos, sin embargo, en los wrappers para Android solo disponemos de la revisión.

`BackgroundSubtractorMOG` está basado en el modelo Gaussian Mixture (GMM). Se trata de un modelo compuesto por la suma de varias distribuciones Gaussianas que, correctamente elegidas, permiten modelar cualquier distribución. [^coursera:gmm] El algoritmo de substracción del fondo fue propuesto en el artículo [^art:yao_improved_2014] y modela cada píxel del fondo como la mezcla de _K_ distribuciones Gaussianas. Los pesos de la mezcla representan las proporciones de tiempo que el color de ese píxel se ha mantenido en la escena. Siendo los colores de fondo más probables los que más permanecen y son más estáticos. [^opencv:bs_tutorial]

`BackgroundSubtractorMOG2` se basa en los mismos principios que su antecesor pero implementa una mejora sustancial. Es el propio algoritmo el que selecciona el número adecuado de distribuciones Gaussianas necesarias para modelar cada píxel. De esta manera, se mejora notablemente la adaptabilidad a variaciones en la escena. Fue propuesto en los artículos [^art:zivkovic_improved_2004] y [^art:zivkovic_efficient_2006].

El código fuente de este algoritmo está disponible en [^github:background_segm] (interfaz) y [^github:bgfg_gaussmix2] (implementación).


La clase de OpenCV que lo implementa es `BackgroundSubtractorMOG2`. Posee los siguientes parámetros configurables: [^opencv:mog2]

- `history`: número de fotogramas recientes que afectan al modelo del fondo. Se representa en la literatura como `T`. Por defecto, 500 fotogramas.
- `learningRate`: valor entre 0 y 1 que indica como de rápido aprende el modelo. Si se establece un valor de -1 el algoritmo elige automáticamente el ratio. 0 significa que el modelo del fondo no se actualiza para nada, mientras que 1 supone que el modelo del fondo se reinicializa completamente cada nuevo fotograma. En la literatura podemos encontrar este parámetro como `alfa`. Si el intervalo que se quiere considerar es `history`, se debe establecer `alfa=1/history` (valor por defecto). También se pueden mejorar los resultados iniciales estableciendo `alfa=1` en el instante 0 e ir decrementándolo hasta `alfa=1/history`. De esta manera, en el inicio aprende rápidamente, pero una vez estabilizada la situación las variaciones afectan menos al modelo.
- `backgroundRatio`: si un pixel del primer plano permanece con un valor semi-constante durante `backgroundRatio*history` fotogramas, es considerado fondo y se añade al modelo del fondo como centro de una nueva componente Gaussiana. En los artículos se hace referencia a este parámetro como `TB`. `TB=0.9` es el valor por defecto.
- `detectShadows`: con un valor verdadero (valor por defecto) detecta las sombras (aumenta ligeramente el tiempo de procesado).
- `shadowThreshold`: el algoritmo detecta las sombras comprobando si un píxel es una versión oscurecida del fondo. Este parámetro define cómo de oscura puede ser la sombra como máximo. Por ejemplo, un valor de 0.5 (valor por defecto) significa que si un píxel es más del doble de oscuro, entonces no se considerará sombra. En los artículos se representa como `Tau`.
- `shadowValue`: es el valor utilizado para marcar los píxeles de sombras en la máscara resultante. El valor por defecto es 127. En la máscara devuelta, un valor de 0 siempre se corresponde con un pixel del fondo, mientras que un valor de 255 con un píxel del primer plano.
- `nMixtures`: número máximo de componentes Gaussianas para modelar el modelo del fondo. El número actual se determina dinámicamente para cada píxel. Por defecto, 5.
- `varThreshold`: umbral utilizado en el cálculo de la distancia cuadrada de Mahalanobis entre el píxel y el modelo del fondo para decidir si una muestra está bien descrita por el modelo o no. Este parámetro no afecta a la actualización del modelo del fondo. Se representa como `Cthr`. Por defecto, 16.
- `varThresholdGen`: umbral sobre la distancia cuadrada de Mahalanobis entre el píxel y el modelo para ayudar a decidir si un píxel está cercano a alguna de las componentes del modelo. Si no es así, es considerado como primer plano o añadido como centro de una nueva componente (dependiendo del `backgroundRatio`). Se representa como `Tg` y su valor por defecto es 9. Un valor menor genera más componentes Gaussianas, mientras que un valor mayor genera menos.
- `complexityReductionThreshold`: este parámetro define el número de muestras necesarias para probar que una componente existe. Se representa como `CT`. Su valor por defecto es `CT=0.05`. Si se establece su valor a 0 se obtiene un algoritmo similar al de Stauffer & Grimson (no se reduce el número de componentes).
- `varInit`: varianza inicial de cada componente Gaussiana. Afecta a la velocidad de adaptación. Se debe ajustar teniendo en cuenta la desviación estandar de las imágenes. Por defecto es 15.
- `varMin`: varianza mínima. Por defecto, 4.
- `varMax`: varianza máxima. Por defecto, `5*varInit`.

De todos ellos, los parámetros más importantes a ajustar son `history` o `learningRate`, `varThreshold` y `detectShadows`.

La parametrización correcta de este algoritmo es clave para su buen funcionamiento. Por ello, durante las pruebas se integró en nuestra aplicación de desarrollo, permitiendo variar todos estos parámetros en tiempo real. De esta manera. se pudo elegir le mejor configuración para nuestro problema concreto.

Una vez parametrizado correctamente, vimos como este algoritmo era el que mejores resultados nos proporcionaba. Con un tiempo de ejecución en nuestro equipo de pruebas de entorno a 4ms/frame, mucho menor que el proporcionado por `BackgroundSubtractorKNN`, de entorno a 25ms/frame. El algoritmo detectaba correctamente las abejas, era resistente al ruido que afectaba al algoritmo de substracción de los fotogramas anteriores y además era capaz de diferenciar una abeja de su sombra. Por todos estos motivos, se seleccionó para la fase de substracción del fondo.

#### BackgroundSubtractorKNN
Se trata de un método que se basa en el algoritmo de clasificación supervisada _K nearest neighbors_ (k-nn). El algoritmo fue propuesto en el artículo [^art:zivkovic_efficient_2006]. Y de acuerdo con sus conclusiones, es muy eficiente cuando el número de píxeles que se corresponden con el primer plano es bajo.

La clase de OpenCV que lo implementa es `BackgroundSubtractorKNN`. Los parámetros más importantes son:
- `history`: número de fotogramas recientes que afectan al modelo del fondo.
- `dist2Threshold`: umbral de la distancia al cuadrado entre el píxel y la muestra para decidir si un píxel está cerca de esa muestra.
- `detectShadows`: con un valor verdadero detecta las sombras (aumenta considerablemente el tiempo de procesado).

En nuestras pruebas, el algoritmo proporcionaba unos resultados buenos pero su tiempo de ejecución era muy elevado (entorno a 25ms/frame). Como el tiempo de ejecución es un factor clave en nuestro proyecto, se descartó el uso de este algoritmo.

#### Otros algoritmos
La implementación original de OpenCV implementa otros dos algoritmos más que no están disponibles a través de los wrappers de Android.

- `BackgroundSubtractorGMG` es un algoritmo que combina una estimación estadística del fondo de la imagen junto con una segmentación Bayesiana píxel a píxel. [^opencv:bs_tutorial]

- `BackgroundSubtractorFGD` está disponible en la versión para CUDA. Utiliza la regla de decisión de Bayes para clasificar los elementos del fondo y los del primer plano atendiendo a sus vectores de características. [^art:li_foreground_2003]

### 3º Thresholding
asdf







<!--- References -->

[^wiki:bs]: https://en.wikipedia.org/wiki/Background_subtraction
[^opencv:bs_tutorial]: http://docs.opencv.org/master/db/d5c/tutorial_py_bg_subtraction.html
[^book:opencv_java]: https://www.packtpub.com/application-development/opencv-30-computer-vision-java
[^programarfacil:detmov]: http://programarfacil.com/blog/vision-artificial/deteccion-de-movimiento-con-opencv-python/
[^coursera:gmm]: https://www.coursera.org/learn/robotics-learning/lecture/XG0WD/1-4-1-gaussian-mixture-model-gmm/
[^art:yao_improved_2014]: http://www.ee.surrey.ac.uk/CVSSP/Publications/papers/KaewTraKulPong-AVBS01.pdf
[^art:zivkovic_improved_2004]: http://www.zoranz.net/Publications/zivkovic2004ICPR.pdf
[^art:zivkovic_efficient_2006]: http://www.zoranz.net/Publications/zivkovicPRL2006.pdf
[^art:li_foreground_2003]: http://citeseerx.ist.psu.edu/viewdoc/download?doi=10.1.1.62.8313&rep=rep1&type=pdf
[^github:background_segm]: https://github.com/opencv/opencv/blob/master/modules/video/include/opencv2/video/background_segm.hpp
[^github:bgfg_gaussmix2]: https://github.com/opencv/opencv/blob/master/modules/video/src/bgfg_gaussmix2.cpp
[^opencv:mog2]: http://docs.opencv.org/3.1.0/d7/d7b/classcv_1_1BackgroundSubtractorMOG2.html