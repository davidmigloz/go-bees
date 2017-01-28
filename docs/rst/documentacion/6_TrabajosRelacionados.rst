Trabajos relacionados
=====================

Como se comentó en la introducción, los intentos de automatizar el
proceso de monitorización de la actividad de una colmena se remontan
hasta principios del siglo pasado. Sin embargo, no es hasta 2008 cuando
se introduce la visión artificial en este campo. A continuación, se
exponen los artículos científicos relacionados publicados hasta la
fecha, así como proyectos con objetivos similares.

Artículos científicos
---------------------

Video Monitoring of Honey Bee Colonies at the Hive Entrance
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

Se trata del primer artículo publicado sobre el tema (año 2008). Los
autores fueron Jason Campbell, Lily Mummert y Rahul Sukthankar del
*Intel Research Pittsburgh*. En él proponen un método de visión
artificial para monitorizar las entradas y salidas de abejas en una
colmena, consiguiendo diferenciar las que entran de las que salen. Se
describen los desafíos técnicos que supuso y la solución a la que
llegaron finalmente [art:campbell2008]_.

Detecting and tracking honeybees in 3D at the beehive entrance using stereo vision
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

En 2013, Guillaume Chiron, Petra Gomez-Krämer y Ménard Michel publicaron
un artículo en *EURASIP Journal on Image and Video Processing,* donde
proponían un método para la monitorización de abejas a la entrada de una
colmena basado en un sistema de tiempo real con visión estereoscópica.
Gracias al cual podían obtener una representación en tres dimensiones de
las trayectorias de las abejas [art:chiron2013]_.

Image Processing for Honey Bee hive Health Monitoring
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

El último artículo publicado data del año 2015 por Rahman Tashakkori y
Ahmad Ghadiri de la *Appalachian State University*. En él, mejoran el
método de detección propuesto en [art:campbell2008]_ y lo utilizan para
estimar el número de abejas que habrá en un instante de tiempo dado [art:tashakkori2015]_.

Comparación
~~~~~~~~~~~

+--------------------------+---+----+-------------------+--------------+---------------+
| Artículo                 | A | Ci | Detección de      | Conteo de    | Tracking      |
|                          | ñ | ta | movimiento        | abejas       |               |
|                          | o | s  |                   |              |               |
+==========================+===+====+===================+==============+===============+
| *Video Monitoring of     | 2 | 24 | *Adaptative       | *Template-ba | *Maximum      |
| Honey Bee Colonies at    | 0 |    | background        | sed          | weighted      |
| the Hive Entrance*       | 0 |    | subtraction.*     | method.*     | bipartite     |
|                          | 8 |    |                   |              | graph         |
|                          |   |    |                   |              | matching.*    |
+--------------------------+---+----+-------------------+--------------+---------------+
| *Detecting and tracking  | 2 | 8  | *Adaptative       | *Hybrid 3D   | *Kalman       |
| honeybees in 3D at the   | 0 |    | background        | intensity    | filter* y     |
| beehive entrance using   | 1 |    | subtraction with  | depth        | *Global       |
| stereo vision*           | 3 |    | depth             | segmentation | Nearest       |
|                          |   |    | information.*     | .*           | Neighbor.*    |
+--------------------------+---+----+-------------------+--------------+---------------+
| *Image Processing for    | 2 | 0  | *Averaging a      | *Area-based  | No            |
| Honey Bee hive Health    | 0 |    | background with   | method.*     |               |
| Monitoring*              | 1 |    | illumination      |              |               |
|                          | 5 |    | invariant         |              |               |
|                          |   |    | method.*          |              |               |
+--------------------------+---+----+-------------------+--------------+---------------+
| GoBees                   | 2 | 0  | *Mixture of       | *Area-based  | No            |
|                          | 0 |    | Gaussians method  | method.*     |               |
|                          | 1 |    | (BackgroundSubtra |              |               |
|                          | 7 |    | ctorMOG2).*       |              |               |
+--------------------------+---+----+-------------------+--------------+---------------+

Proyectos
---------

EyesOnHives
~~~~~~~~~~~

EyesOnHives es el principal competidor del proyecto. Se trata de un
producto comercial cuyo fin es la monitorización del estado de salud de
las colmenas mediante su actividad de vuelo. Integra un *hardware*
específico que se encarga de la captación de imágenes y una plataforma
en la nube que las procesa y permite el acceso a los datos.

-  Web del proyecto: http://www.keltronixinc.com

HiveTool
~~~~~~~~

Se trata de un proyecto *OpenSource* que ofrece un conjunto de
herramientas para monitorizar distintos parámetros de una colmena. Una
de estas herramientas es "Bee Counter", un contador de abejas por visión
artificial desarrollado sobre una Raspberry Pi.

-  Web del proyecto: http://hivetool.org/

Fortalezas y debilidades del proyecto
-------------------------------------

+-----------------------------------+-----------+---------------+------------+
| Características                   | GoBees    | EyesOnHives   | HiveTool   |
+===================================+===========+===============+============+
| No requiere *Hardware* específico | ✔         | ✘             | ✘          |
+-----------------------------------+-----------+---------------+------------+
| Instalación sencilla              | ✔         | ✔             | ✘          |
+-----------------------------------+-----------+---------------+------------+
| Procesamiento en local            | ✔         | Parcial       | ✔          |
+-----------------------------------+-----------+---------------+------------+
| No requiere wifi                  | ✔         | ✘             | ✔          |
+-----------------------------------+-----------+---------------+------------+
| No requiere red eléctrica         | ✔         | ✔             | ✔          |
+-----------------------------------+-----------+---------------+------------+
| Localización GPS                  | ✔         | ✘             | ✘          |
+-----------------------------------+-----------+---------------+------------+
| Plataformas                       | Android   | Web App       | Linux      |
+-----------------------------------+-----------+---------------+------------+

Las principales fortalezas del proyecto son:

-  No se necesita adquirir ningún *hardware* específico como en el resto
   de proyectos, simplemente se necesita un *smartphone* con Android.
   Esto hace el proyecto mucho más accesible a los potenciales usuarios.

-  La instalación es muy sencilla. Únicamente se requiere un trípode o 
   cualquier otro tipo de soporte que permita sujetar el *smartphone* en 
   posición cenital.

-  El procesamiento de las imágenes se realiza en local no en un
   servidor. Considerando que los colmenares suelen estar en medio del
   monte, no podemos requerir una conexión *wifi* como necesita
   EyesOnHives y el envío de vídeo mediante tecnologías 3G/4G supondría
   un coste económico muy elevado.

-  No requiere estar conectado a la red eléctrica. El *smartphone*
   cuenta con su propia batería. El consumo de la aplicación no es muy
   elevado al estar la pantalla apagada durante la monitorización. Aun
   así, se pueden utilizar *powerbanks* (baterías portátiles) en caso de
   ser necesarios.

-  El *smartphone* tiene integradas varias tecnologías de transmisión de
   información. Lo da la posibilidad de crear una plataforma que
   centralice la recogida de datos de varios dispositivos sin importar
   su localización.

-  Relacionado con el punto anterior, el *smartphone* nos permite estar
   conectados a internet, posibilitándonos ampliar la información que
   maneja nuestra aplicación. Por ejemplo, podemos acceder a la
   información meteorológica en tiempo real.

-  El GPS del *smartphone* nos permite localizar geográficamente la
   monitorización y, por tanto, la información meteorológica. Además,
   puede ser de utilidad en caso de robo, gracias a aplicaciones como
   *Android Device Manager*, Cerberus, etc. que permiten localizar el
   dispositivo de forma remota.

Las principales debilidades son:

-  Actualmente solo se encuentra disponible para Android. Aunque en una
   segunda fase del proyecto se creará una plataforma en la nube que
   centralice todos los datos y una aplicación web que permita acceder a
   ellos.

-  El utilizar un *smartphone* como soporte *hardware* tiene sus
   ventajas, pero también sus inconvenientes. La cámara no tiene el
   mismo rendimiento que una cámara diseñada específicamente para esta
   tarea. Esto nos ha limitado en las técnicas de visión artificial que
   hemos podido aplicar, por no disponer de imágenes con la suficiente
   nitidez.
   
.. References

.. [art:campbell2008]
   http://homepages.inf.ed.ac.uk/rbf/VAIB08PAPERS/vaib9_mummert.pdf
.. [art:chiron2013]
   http://jivp.eurasipjournals.springeropen.com/articles/10.1186/1687-5281-2013-59
.. [art:tashakkori2015]
   http://ieeexplore.ieee.org/document/7133029/?arnumber=7133029
