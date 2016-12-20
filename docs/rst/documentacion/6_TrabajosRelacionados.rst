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
autores fueron Jason Campbell, Lily Mummert y Rahul Sukthankar. En él
proponen un método de visión artificial para monitorizar las entradas y
salidas de abejas en una colmena. Se describen los desafíos técnicos que
supuso y la solución a la que llegaron finalmente.

Detecting and tracking honeybees in 3D at the beehive entrance using stereo vision
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

En 2013, Guillaume Chiron, Petra Gomez-Krämer y Ménard Michel publicaron
un artículo en donde proponían un método para la monitorización de
abejas a la entrada de una colmena basado en un sistema de tiempo real
con visión estereoscópica, gracias al cual podían obtener una
representación en tres dimensiones de las trayectorias de las abejas.

Image Processing for Honey Bee hive Health Monitoring
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

El último artículo publicado data del año 2015 por Rahman Tashakkori y
Ahmad Ghadiri. En él, mejoran el método propuesto por Campbell et al. y
desarrollan otro que permite estimar el número de abejas que habrá en un
instante de tiempo dado.

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

+--------------------------+-----------+---------------+------------+
|                          | GoBees    | EyesOnHives   | HiveTool   |
+==========================+===========+===============+============+
| *Hardware* específico    | NO        | SI            | SI         |
+--------------------------+-----------+---------------+------------+
| Instalación sencilla     | SI        | SI            | NO         |
+--------------------------+-----------+---------------+------------+
| Procesamiento en local   | SI        | Parcial       | SI         |
+--------------------------+-----------+---------------+------------+
| Requiere wifi            | NO        | SI            | NO         |
+--------------------------+-----------+---------------+------------+
| Requiere red eléctrica   | NO        | SI            | NO         |
+--------------------------+-----------+---------------+------------+
| Localización GPS         | SI        | NO            | NO         |
+--------------------------+-----------+---------------+------------+
| Plataformas              | Android   | Web App       | Linux      |
+--------------------------+-----------+---------------+------------+

Las principales fortalezas del proyecto son:

-  No se necesita adquirir ningún *hardware* específico como en el resto
   de proyectos, simplemente se necesita un *smartphone* con Android.
   Esto hace el proyecto mucho más accesible a los potenciales usuarios.
-  La instalación es muy sencilla. Únicamente se requiere un trípode.
-  El procesamiento de las imágenes se realiza en local no en un
   servidor. Considerando que los colmenares suelen estar en medio del
   monte, no podemos requerir una conexión *wifi* como necesita
   EyesOnHives y el envío de vídeo mediante tecnologías 3G/4G supone un
   coste económico muy elevado.
-  No requiere estar conectado a la red eléctrica. El *smartphone*
   cuenta con su propia batería. El consumo de la aplicación no es muy
   elevado al estar la pantalla apagada durante la monitorización. Aun
   así, se pueden utilizar *powerbanks* en caso de ser necesarios.
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
   puede ser de utilidad en caso de robo.

Las principales debilidades son:

-  Actualmente solo se encuentra disponible para Android. Aunque en una
   segunda fase del proyecto se creará una plataforma en la nube que
   centralice todos los datos y una aplicación web que permita acceder a
   ellos.
-  Como la cámara varia en gran medida dependiendo del *smartphone*
   utilizado, se pueden encontrar problemas en este sentido.
