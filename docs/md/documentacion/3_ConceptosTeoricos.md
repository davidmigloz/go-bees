# Conceptos teóricos

{En aquellos proyectos que necesiten para su comprensión y desarrollo de unos conceptos teóricos de una determinada materia o de un determinado dominio de conocimiento, debe existir un apartado que sintetice dichos conceptos.}

## Visión artificial

### 1º Preprocesado

Antes de aplicar los substracción del fondo, es recomendable realizar un preprocesado de los fotogramas para facilitar el procesado, minimizar el ruido y optimizar los resultados. En primer lugar, se convertirán los fotogramas a escala de grises. Y en segundo lugar, aplicaremos un desenfoque gausiano para minimizar el ruido provocado por la propia cámara y por la iluminación.

#### Conversión de RGB a escala de grises
asdf

#### Desenfoque gausiano
asdf


### 2º Substracción del fondo

En un sistema de vigilancia resulta de gran interés el poder extraer los objetos en movimiento. Esto se conoce como extracción del fondo, en inglés _background subtraction_ o _foreground detection_. Entendemos por esto la clasificación de todos los píxeles de un determinado frame bien como fondo, o como primer plano. [ [1] ] En primer plano se engloban a todos los objetos en movimiento, mientras que en el fondo se encuentran todos los objetos estáticos junto con posibles sombras, cambios de iluminación u otros objetos en movimiento que no son de interés, como puede ser la rama de un árbol balanceándose por el viento. Se trata de un paso crítico, ya que algoritmos posteriores (detección y tracking) dependen en gran medida de los resultados de este.

En nuestro proyecto, la toma de imágenes se realiza mediante una cámara estática. Esto facilita en parte la detección del fondo, ya que este se corresponderá con todos los píxeles estáticos. Sin embargo, trabajamos en un entorno al aire libre, tenemos que lidiar también con cambios de iluminación, sombras, u otros objetos móviles que no son de nuestro interés (falsos positivos).

Con OpenCV para Android podemos implementar varios algoritmos básicos de extracción del fondo que comentaremos a continuación. Además, nos proporciona la implementación de dos algoritmos más sofisticados: `BackgroundSubtractorMOG` y `BackgroundSubtractorKNN`.

#### Substracción con imagen de referencia
Se parte de una imagen de referencia del fondo, en la que no haya ningún objeto en movimiento. A partir de esta, se obtienen los elementos en movimiento substrayendo a cada fotograma la imagen tomada como referencia.

Este método, al tomar un modelo del fondo tan sencillo y estático, es muy vulnerable a cambios en la escena (iluminación, sombras, objetos del fondo con ligeros movimientos, pequeñas oscilaciones de la cámara, etc). Sin embargo, ofrece muy buenos resultados cuando se trabaja en una escena con la iluminación y los elementos controlados. [ [4] ]

Para implementar este algoritmo con OpenCV, se hace uso de la función `Core.absdiff()`.

#### Substracción del fotograma anterior
En este método el modelo del fondo se extrae del fotograma anterior. De tal manera que a cada nuevo fotograma se le substrae el anterior.

De esta manera se mejora la respuesta a cambios en la escena, como los cambios de iluminación. Sin embargo, si un objeto en movimiento se queda estático en la imagen, este deja de ser detectado. 

La implementación se realiza como en la técnica anterior, variando el modelo del fondo.

#### Substracción de los fotogramas anteriores
Una mejora interesante del algoritmo anterior supone tomar como modelo del fondo un acumulado de los frames anteriores de acuerdo a un ratio de aprendizaje. De esta forma, se puede lidiar con cambios en el fondo de la imagen dinámicamente. El modelo se calcula de acuerdo a la siguiente fórmula:

![](https://latex.codecogs.com/gif.latex?%5CLARGE%20u_t%20%3D%20%281-%5Calpha%20%29u_%7Bt-1%7D&plus;%5Calpha%5C%20p_t)

`u_t = (1-\alpha )u_{t-1}+\alpha\ p_t`

Donde  p_t es el nuevo valor del píxel, u_{t-1} es la media del fondo en el instante t-1, u_t es la nueva media del fondo y \alpha es el ratio de aprendizaje (cómo de rápido olvida los frames anteriores). [ [2] ]

OpenCV provee la función `Imgproc.accumulateWeighted()` que implementa por nosotros la fórmula anterior. Haciendo uso de esta función y de la utilizada en la sección anterior podemos implementar este algoritmo.
 
#### BackgroundSubtractorMOG2

`BackgroundSubtractorMOG2` es una mejora del algoritmo `BackgroundSubtractorMOG`. En la versión original de OpenCV se encuentran implementados ambos, sin embargo, en los wrappers para Android solo disponemos de la revisión.

`BackgroundSubtractorMOG` está basado en el modelo Gaussian Mixture (GMM). Se trata de un modelo compuesto por la suma de varias distribuciones Gausianas que, correctamente elegidas, permiten modelar cualquier distribución. [ [5] ] El algoritmo de substracción del fondo fue propuesto en el artículo [ [6] ] y modela cada píxel del fondo como la mezcla de _K_ distribuciones Gausianas. Los pesos de la mezcla representan las proporciones de tiempo que el color de ese píxel se ha mantenido en la escena. Siendo los colores de fondo más probables los que más permanecen y son más estáticos. [ [2] ]

`BackgroundSubtractorMOG2` se basa en los mismos principios que su antecesor pero implementa una mejora sustancial. Es el propio algoritmo el que selecciona el número adecuado de distribuciones Gausianas necesarias para modelar cada píxel. De esta manera, se mejora notablemente la adaptabilidad a variaciones de la escena. Fue propuesto en los artículos [ [7] ] y [ [8] ].

La función de OpenCV que lo implementa es `Video.createBackgroundSubtractorMOG2()`. Posee tres parámetros configurables:

- _history_: tamaño del histórico.
- _varThreshold_: umbral de la distancia de Mahalanobis al cuadrado entre el píxel y el modelo para decidir si un píxel está bien descrito por el fondo.
- _detectShadows_: con un valor verdadero (True) detecta las sombras (aumenta considerablemente el tiempo de procesado).

#### BackgroundSubtractorKNN
Se trata de un método que se basa en el algoritmo de clasificación supervisada K nearest neighbors (k-nn). El algoritmo fue propuesto en el artículo [ [8] ]. Y de acuerdo con sus conclusiones, es muy eficiente cuando el número de píxeles que se corresponden con el primer plano es bajo.

La función de OpenCV que lo implementa es `Video.createBackgroundSubtractorKNN()`. Posee tres parámetros configurables:
-_history_: tamaño del histórico.
-_dist2Threshold_: umbral de la distancia al cuadrado entre el píxel y la muestra para decidir si un píxel está cerca de esa muestra.
-_detectShadows_: con un valor verdadero (True) detecta las sombras (aumenta considerablemente el tiempo de procesado).

#### Otros algoritmos
La implementación original de OpenCV implementa otros dos algoritmos más que no están disponibles a través de los wrappers de Android. 

- `BackgroundSubtractorGMG` es un algoritmo que combina una estimación estadística del fondo de la imagen junto con una segmentación Bayesiana píxel a píxel. [ [2] ]

- `BackgroundSubtractorFGD` está disponible en la versión para CUDA. Utiliza la regla de decisión de Bayes para clasificar los elementos del fondo y los del primer plano atendiendo a sus vectores de características. [ [9] ] 

### 3º Thresholding
asdf







<!--- References -->

[1]: https://en.wikipedia.org/wiki/Background_subtraction?23-10-2016
[2]: http://docs.opencv.org/master/db/d5c/tutorial_py_bg_subtraction.html?23-10-2016
[3]: https://www.packtpub.com/application-development/opencv-30-computer-vision-java
[4]: http://programarfacil.com/blog/vision-artificial/deteccion-de-movimiento-con-opencv-python/?05-10-2016
[5]: https://www.coursera.org/learn/robotics-learning/lecture/XG0WD/1-4-1-gaussian-mixture-model-gmm/?05-10-2016
[6]: http://www.ee.surrey.ac.uk/CVSSP/Publications/papers/KaewTraKulPong-AVBS01.pdf?05-10-2016
[7]: http://www.zoranz.net/Publications/zivkovic2004ICPR.pdf?05-10-2016
[8]: http://www.zoranz.net/Publications/zivkovicPRL2006.pdf?05-10-2016
[9]: http://citeseerx.ist.psu.edu/viewdoc/download?doi=10.1.1.62.8313&rep=rep1&type=pdf&05-10-2016