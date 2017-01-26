Técnicas y herramientas
=======================

Metodologías
------------

Scrum
~~~~~

Scrum es un marco de trabajo para el desarrollo de software que se
engloba dentro de las metodologías ágiles. Aplica una estrategia de
trabajo iterativa e incremental a través de iteraciones (*sprints*) y
revisiones.

*Test-Driven Development* (TDD)
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

Es una práctica de desarrollo de software que se basa en la repetición
de un ciclo corto de desarrollo: transformar requerimientos a test,
desarrollar el código necesario para pasar los test y posteriormente
refactorizar el código. Esta práctica obliga a los desarrolladores a
analizar cuidadosamente las especificaciones antes de empezar a escribir
código, fomenta la escritura de test y la simplicidad de código y
aumenta la productividad. Como resultado se obtiene software más seguro
y de mayor calidad.

Gitflow
~~~~~~~

`Gitflow <http://nvie.com/posts/a-successful-git-branching-model/>`__ es
un flujo de trabajo con Git que define un modelo estricto de ramas
diseñado en torno a los lanzamientos de proyecto. En la rama *main* se
hospeda la última versión estable del proyecto. La rama *develop*
contiene los últimos desarrollos realizados para el siguiente
lanzamiento. Por cada característica que se vaya a implementar se crea
una *feature branch*. La preparación del siguiente lanzamiento se
realiza en una *release branch*. Si aparece un fallo en producción, este
se soluciona en una *hotfix branch*.

Patrones de diseño
------------------

Model-View-Presenter (MVP)
~~~~~~~~~~~~~~~~~~~~~~~~~~

MVP es un patrón de arquitectura derivado del *Model–View–Controller*
(MVC). Permite separar los datos internos del modelo de una vista pasiva
y enlazarlos mediante el *presenter* que maneja toda la lógica de la
aplicación. Posee tres capas:

-  **Model**: almacena y proporciona los datos internos.

-  *View*: maneja la visualización de los datos (del modelo). Propaga
   las acciones de usuario al *presenter*.

-  *Presenter*: enlaza las dos capas anteriores. Sincroniza los datos
   mostrados en la vista con los almacenados en el modelo y actúa ante
   los eventos de usuario propagados por la vista.

|MVP|
   
.. |MVP| image:: ../../img/mvp.png

Patrón repositorio
~~~~~~~~~~~~~~~~~~

El patrón repositorio proporciona una abstracción de la implementación
del acceso a datos con el objetivo de que este sea transparente a la
lógica de negocio de la aplicación. Por ejemplo, las fuentes de datos
pueden ser una base de datos, un *web service*, etc. El repositorio
media entre la capa de acceso a datos y la lógica de negocio de tal
forma que no existe ninguna dependencia entre ellas. Consiguiendo
desacoplar, mantener y testear más fácilmente el código y permitiendo la
reutilización del acceso a datos desde cualquier cliente.

|repositorio|

.. |repositorio| image:: ../../img/repository_pattern.png

Control de versiones
--------------------

-  Herramientas consideradas: `Git <https://git-scm.com/>`__ y
   `Subversion <https://subversion.apache.org/>`__.

-  Herramienta elegida: `Git <https://git-scm.com/>`__.

Git es un sistema de control de versiones distribuido. A día de hoy, es
el sistema con mayor número de usuarios. La principal diferencia con
Subversion es su carácter descentralizado, que permite a cada
desarrollador tener una copia en local del repositorio completo. Git
está licenciado bajo la licencia de software libre GNU LGPL v2.1.

Hosting del repositorio
-----------------------

-  Herramientas consideradas: `GitHub <https://github.com/>`__,
   `Bitbucket <https://bitbucket.org/>`__ y
   `GitLab <https://gitlab.com/>`__.

-  Herramienta elegida: `GitHub <https://github.com/>`__.

GitHub es la plataforma web de hospedaje de repositorios por excelencia.
Ofrece todas las funcionalidades de Git, revisión de código,
documentación, *bug tracking*, gestión de tareas, *wikis*, red social...
y numerosas integraciones con otros servicios. Es gratuita para
proyectos *open source*.

Utilizamos GitHub como plataforma principal donde hospedamos el código
del proyecto, la gestión de proyecto (gracias a ZenHub) y la
documentación. Además, el repositorio está integrado con varios
servicios de integración continua.

Gestión del proyecto
--------------------

-  Herramientas consideradas: `ZenHub <https://www.zenhub.com/>`__,
   `Trello <https://trello.com/>`__, `Waffle <https://waffle.io/>`__,
   `VersionOne <https://www.versionone.com/>`__,
   `XP-Dev <https://xp-dev.com/>`__ y `GitHub
   Projects <https://github.com/>`__.

-  Herramienta elegida: `ZenHub <https://www.zenhub.com/>`__.

ZenHub es una herramienta de gestión de proyectos totalmente integrada
en GitHub. Proporciona un tablero canvas en donde cada tarea
representada se corresponde con un *issue* nativo de GitHub. Cada tarea
se puede priorizar dependiendo de su posición en la lista, se le puede
asignar una estimación, uno o varios responsables y el *sprint* al que
pertenece. ZenHub también permite visualizar el gráfico *burndown* de
cada *sprint*. Es gratuita para proyectos pequeños (máx. 5
colaboradores) o proyectos *open source*.

Comunicación
------------

-  Herramientas consideradas: email y
   `Slack <https://gobees.slack.com/>`__.

-  Herramienta elegida: `Slack <https://gobees.slack.com/>`__.

Slack es una herramienta de colaboración de equipos que ofrece salas de
chat, mensajes directos y llamadas VoIP. Posee un buscador que permite
encontrar todo el contenido generado dentro de Slack. Además, ofrece un
gran número de integraciones con otros servicios. En nuestro proyecto
vamos a utilizar la integración con GitHub para crear un canal que sirva
de log de todas las acciones realizadas en GitHub. Slack ofrece una
versión gratuita que provee las características principales.

Entorno de desarrollo integrado (IDE)
-------------------------------------

Java
~~~~

-  Herramientas consideradas: `IntelliJ
   IDEA <https://www.jetbrains.com/idea/>`__ y
   `Eclipse <https://eclipse.org/>`__.

-  Herramienta elegida: `IntelliJ
   IDEA <https://www.jetbrains.com/idea/>`__.

IntelliJ IDEA es un IDE para Java desarrollado por JetBrains. Posee un
gran número de herramientas para facilitar el proceso de escritura,
revisión y factorización del código. Además, permite la integración de
diferentes herramientas y posee un ecosistema de *plugins* para ampliar
su funcionalidad. Su versión *community* está disponible bajo la
licencia Apache 2. Aunque también es posible adquirir la versión
*Ultimate* gratuitamente si se es estudiante.

Android
~~~~~~~

-  Herramientas consideradas: `Android
   Studio <https://developer.android.com/studio/index.html>`__ y
   `Eclipse <https://eclipse.org/>`__.

-  Herramienta elegida: `Android
   Studio <https://developer.android.com/studio/index.html>`__.

Android Studio es el IDE oficial para el desarrollo de aplicaciones
Android. Está basado en IntelliJ IDEA de JetBrains. Proporciona soporte
para Gradle, emulador, editor de *layouts*, refactorizaciones
específicas de Android, herramientas Lint para detectar problemas de
rendimiento, uso, compatibilidad de versión, etc. Se distribuye bajo la
licencia Apache 2.

Markdown
~~~~~~~~

-  Herramientas consideradas: `StackEdit <https://stackedit.io/>`__ y
   `Haroopad <http://pad.haroopress.com/>`__.

-  Herramienta elegida: `Haroopad <http://pad.haroopress.com/>`__.

Haroopad es un editor de documentos Markdown. Soporta Github Flavored
Markdown y Mathematics Expression, además de contar con un gran número
de extensiones. Se distribuye bajo licencia GNU GPL v3.0.

LaTeX
~~~~~

-  Herramientas consideradas:
   `ShareLaTeX <https://www.sharelatex.com/>`__ y
   `Texmaker <http://www.xm1math.net/texmaker/>`__.

-  Herramienta elegida: `Texmaker <http://www.xm1math.net/texmaker/>`__.

Texmaker es un editor gratuito y multiplataforma para LaTeX. Integra la
mayoría de herramientas necesarias para la escritura de documentos en
LaTeX (PdfLaTeX , BibTeX, makeindex, etx). Además, incluye corrector
ortográfico, auto-completado, resaltado de sintaxis, visor de PDFs
integrado, etc. Está licenciado bajo GNU GPL v2.

Documentación
-------------

-  Herramientas consideradas: `Microsoft
   Word <https://products.office.com/es-es/word>`__,
   `LibreOffice <https://es.libreoffice.org/>`__,
   `LaTeX <https://www.latex-project.org/>`__,
   `Markdown <http://daringfireball.net/projects/markdown/>`__, `GitHub
   Wikis <https://github.com/>`__.

-  Herramienta elegida:
   `Markdown <http://daringfireball.net/projects/markdown/>`__ +
   `LaTeX <https://www.latex-project.org/>`__.

La documentación se ha desarrollado en Markdown para integrarla con el
servicio de documentación continua `Read the
Docs <https://readthedocs.org/>`__. Una vez terminada, se ha exportado a
LaTeX utilizando el conversor `Pandoc <http://pandoc.org/>`__.

Markdown es un lenguaje de marcado ligero en texto plano que puede ser
exportado a numerosos formatos como HTML o PDF. Su filosofía es que el
lenguaje de marcado sea fácil de escribir y leer. Markdown es
ampliamente utilizado para la escritura de archivos README, en foros
como StackOverflow o en herramientas de comunicación como Slack.

LaTeX es un sistema de composición de textos que genera documentos con
una alta calidad tipográfica. Es ampliamente utilizado para la
generación de artículos y libros científicos, principalmente por su
potencia a la hora de representar expresiones matemáticas.

Servicios de integración continua
---------------------------------

Compilación y testeo
~~~~~~~~~~~~~~~~~~~~

-  Herramientas consideradas: `TravisCI <https://travis-ci.org/>`__ y
   `CircleCI <https://circleci.com/>`__.

-  Herramienta elegida: `TravisCI <https://travis-ci.org/>`__.

Travis es una plataforma de integración continua en la nube para
proyectos alojados en GitHub. Permite realizar una *build* del proyecto
y testearla automáticamente cada vez que se realiza un *commit*,
devolviendo un informe con los resultados. Es gratuita para proyectos
*open source*.

Cobertura de código
~~~~~~~~~~~~~~~~~~~

-  Herramientas consideradas: `Coveralls <https://coveralls.io/>`__ y
   `Codecov <https://codecov.io/>`__.

-  Herramienta elegida: `Codecov <https://codecov.io/>`__.

Codecov es una herramienta que permite medir el porcentaje de código que
está cubierto por un test. Además, realiza representaciones visuales de
la cobertura y gráficos de su evolución. Posee una extensión de
navegador para GitHub que permite visualizar por cada archivo de código
que líneas están cubiertas por un test y cuáles no. Es gratuita para
proyectos *open source*.

Calidad del código
~~~~~~~~~~~~~~~~~~

-  Herramientas consideradas: `Codeclimate <https://codeclimate.com/>`__,
   `SonarQube <https://sonarqube.com/>`__ 
   y `Codacy <https://www.codacy.com/>`__.

-  Herramientas elegidas: `Codeclimate <https://codeclimate.com/>`__ y
   `SonarQube <https://sonarqube.com/>`__.

Codeclimate es una herramienta que realiza revisiones de código
automáticamente. Es gratuita para proyectos *open source*. En nuestro
proyecto hemos activado los siguientes motores de chequeo:
`checkstyle <https://docs.codeclimate.com/docs/checkstyle>`__,
`fixme <https://docs.codeclimate.com/docs/fixme>`__,
`markdownlint <https://docs.codeclimate.com/docs/markdownlint>`__ y
`pmd <https://docs.codeclimate.com/docs/pmd>`__.

SonarQube es una plataforma de código abierto para la revisión continua 
de la calidad de código. Permite detectar código duplicado, violaciones 
de estándares, cobertura de tests unitarios, *bugs* potenciales, etc. 

Revisión de dependencias
~~~~~~~~~~~~~~~~~~~~~~~~

-  Herramientas consideradas:
   `VersionEye <https://www.versioneye.com/>`__.

-  Herramienta elegida: `VersionEye <https://www.versioneye.com/>`__.

VersionEye es una herramienta que monitoriza las dependencias del
proyecto y envía notificaciones cuando alguna de estas está
desactualizada, es vulnerable o viola la licencia del proyecto. Posee
una versión gratuita con ciertas limitaciones.

Documentación
~~~~~~~~~~~~~

-  Herramientas consideradas: `Read the
   Docs <https://readthedocs.org/>`__.

-  Herramienta elegida: `Read the Docs <https://readthedocs.org/>`__.

Read the Docs es un servicio de documentación continua que permite crear
y hospedar una página web generada a partir de los distintos ficheros
Markdown o  reStructuredText de la documentación. 
Cada vez que se realiza un *commit* en el
repositorio se actualiza la versión hospedada. La página web posee un
buscador, da soporte para diferentes versiones del proyecto y soporta
internacionalización. Además, permite exportar la documentación en 
varios formatos (pdf, epub, html, etc.). El servicio es totalmente 
gratuito, sostenido por donaciones y subscripciones *Gold*.

Sistemas de construcción automática del software
------------------------------------------------

Maven
~~~~~

`Maven <https://maven.apache.org/>`__ es una herramienta para 
automatizar el proceso de construcción del *software* (compilación,
testeo, empaquetado, etc.) enfocada a proyectos Java. Básicamente
describe cómo se tiene que construir el *software* y cuáles son sus
dependencias.

Gradle
~~~~~~

`Gradle <https://gradle.org/>`__ es una herramienta similar a Maven, pero
basada en el lenguaje de programación orientado a objetos Groovy. El
sistema de construcción de Android Studio está basado en Gradle y es
actualmente el único soportado de forma oficial para Android.

Librerías
---------

*Android Support Library*
~~~~~~~~~~~~~~~~~~~~~~~~~

La `librería de soporte de
Android <https://developer.android.com/topic/libraries/support-library/>`__
facilita algunas características que no se incluyen en el *framework*
oficial. Proporciona compatibilidad a versiones antiguas con las últimas
características, incluye elementos para la interfaz adicionales y
utilidades extra.

Espresso
~~~~~~~~

`Espresso <https://google.github.io/android-testing-support-library/docs/espresso/>`__
es un framework de *testing* para Android incluido en la librería de
soporte para *testing* en Android. Provee una API para escribir UI test
que simulen las interacciones de usuario con la app.

Google Guava
~~~~~~~~~~~~

`Google Guava <https://github.com/google/guava>`__ agrupa un conjunto de
librerías comunes para Java. Proporciona utilidades básicas para tareas
cotidianas, una extensión del *Java collections framework* (JCF) y otras
extensiones como programación funcional, almacenamiento en caché,
objetos de rango o *hashing*.

Google Play Services
~~~~~~~~~~~~~~~~~~~~

`Google Play
Services <https://developers.google.com/android/guides/overview>`__ es
una librería que permite a las aplicaciones de terceros utilizar
características de aplicaciones de Google como Maps, Google+, etc. En
nuestro caso se ha hecho uso de su servicio de localización, que utiliza
varias fuentes de datos (GPS, red y wifi) para ubicar el dispositivo
rápidamente.

JavaFX
~~~~~~

`JavaFX <http://docs.oracle.com/javase/8/javase-clienttechnologies.htm>`__ 
es una librería para la creación de interfaces gráficas en Java.

JUnit
~~~~~

`JUnit <http://junit.org/junit4/>`__ es un *framework* para Java
utilizado para realizar pruebas unitarias.

Material Design
~~~~~~~~~~~~~~~

`Material Design <https://material.io/guidelines/>`__ es una guía 
de estilos enfocada a la plataforma Android,
pero aplicable a cualquier otra plataforma. Fue presentada en el Google
I/O 2014 y se adoptó en Android a partir de la versión 5.0 (Lollipop).
Se basa en objetos materiales, piezas colocadas en un espacio (lugar) y
con un tiempo (movimiento) determinado.

Mockito
~~~~~~~

`Mockito <http://mockito.org/>`__ es un *framework* de *mocking* que
permite crear objetos *mock* fácilmente. Estos objetos simulan parte del
comportamiento de una clase. Mockito está basado en EasyMock, mejorando
su sintaxis haciendo los test más simples y fáciles de leer y con
mensajes de error descriptivos.

MPAndroidChart
~~~~~~~~~~~~~~

`MPAndroidChart <https://github.com/PhilJay/MPAndroidChart>`__ es una
librería para la creación de gráficos en Android.

OpenCV
~~~~~~

`OpenCV <www.opencv.org>`__ es un paquete *Open Source* de visión
artificial que contiene más de 2500 librerías de procesamiento de
imágenes y visión artificial, escritas en C/C++ a bajo/medio nivel. Se
distribuye gratuitamente bajo una licencia *BSD* desde hace más de una
década. Posee una comunidad de más de 50.000 usuarios alrededor de todo
el mundo y se ha descargado más de 8 millones de veces.

Aunque OpenCV está escrito en C/C++ posee *wrappers* para varias
plataformas, entre ellas Android, en donde da soporte a las principales
arquitecturas de CPU. Desde hace unos años, también soporta CUDA para el
desarrollo en GPU tanto en escritorio como en móvil, aunque en esta
última el soporte es todavía reducido.

OpenWeatherMaps
~~~~~~~~~~~~~~~

`OpenWeatherMap <http://openweathermap.org/>`__ es un 
servicio online que proporciona información
meteorológica. Está inspirado en OpenStreetMap y su filosofía de hacer
accesible la información a la gente de forma gratuita. Utiliza distintas
fuentes de datos desde estaciones meteorológicas oficiales, de
aeropuertos, radares e incentiva a los propietarios de estaciones
meteorológicas a conectarlas a su red. Proporciona una API que permite
realizar hasta 60 llamadas por segundo de forma gratuita.

PowerMock
~~~~~~~~~

`PowerMock <https://github.com/powermock/powermock>`__ es 
una librería de *testing* que permite la creación de *mocks*
de métodos estáticos, constructores, clases finales o métodos privados.

Realm
~~~~~

`Realm <https://realm.io/products/realm-mobile-database/>`__ es una base
de datos orientada a objetos enfocada a dispositivos móviles. Se definen
como la alternativa a SQLite y presumen de ser más rápidos que cualquier
ORM e incluso que SQLite puro. Posee una API muy intuitiva que facilita
en gran medida el acceso a datos.

Página web
----------

GitHub Pages 
~~~~~~~~~~~~

`GitHub Pages <https://pages.github.com/>`__ es un servicio de hosting estático 
que permite hospedar la página del proyecto en su propio repositorio de GitHub.
Permite utilizar Jekyll, un generador de sitios estáticos. No soporta tecnologías
del lado de servidor como PHP, Ruby, Python, etc.

Bootstrap 
~~~~~~~~~

`Bootstrap <http://getbootstrap.com/>`__ es un *framework* para desarrollo 
*front-end*. Contiene una serie de componentes ya implementados que facilitan y agilizan
diseño. Está desarrollado siguiendo la filosofía *mobile first*.

Otras herramientas
------------------

Mendeley 
~~~~~~~~

`Mendeley <https://www.mendeley.com/>`__ es un gestor de referencias bibliográficas. Permite 
añadir referencias de varias formas, visualizar los documentos, etiquetarlos, compartirlos, etc.
Posteriormente se puede exportar todo el catálogo a un fichero BibTex para ser utilizadas desde LaTeX.

Creately 
~~~~~~~~

`Creately <https://creately.com/>`__ es una aplicación web que permite crear todo tipo de diagramas
altamente personalizables. Aunque posee una versión gratuita limitada, se optó por pagar un mes de 
subscripción al valorar que realmente iba a ser utilidad.

