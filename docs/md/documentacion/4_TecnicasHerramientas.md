Técnicas y herramientas
=======================

Metodologías
------------

### Scrum

Scrum es un marco de trabajo para el desarrollo de software que se engloba dentro de las metodologías ágiles. Aplica una estrategia de trabajo iterativa e incremental a través de iteraciones (_sprints_) y revisiones.

### _Test-driven development_ (TDD)

Es una práctica de desarrollo de software que se basa en la repetición de un ciclo corto de desarrollo: transformar requerimientos a tests, desarrollar el código necesario para pasar los tests y posteriormente refactorizar el código. Esta práctica obliga a los desarrolladores a analizar cuidadosamente las especificaciones antes de empezar a escribir código, fomenta la escritura de tests y la simplicidad de código y aumenta la productividad. Como resultado se obtiene software más seguro y de mayor calidad.

### Gitflow

[Gitflow](http://nvie.com/posts/a-successful-git-branching-model/) es un flujo de trabajo con Git que define un modelo estricto de ramas diseñado entorno a los lanzamientos de proyecto. En la rama _main_ se hospeda la última versión estable del proyecto. La rama _develop_ contiene los últimos desarrollos realizados para el siguiente lanzamiento. Por cada característica que se vaya a implementar se crea una _feature branch_. La preparación del siguiente lanzamiento se realiza en una _release branch_. Si aparece un fallo en producción, este se soluciona en una _hotfix branch_.

Patrones de diseño
------------------

### _Model-View-Presenter_ (MVP)

MVP es un patrón de arquitectura derivado del _Model–View–Controller_ (MVC). Permite separar los datos internos del modelo de una vista pasiva y enlazarlos mediante el _presenter_ que maneja toda la lógica de la aplicación. Posee tres capas:

- Model: almacena y proporciona los datos internos.
- View: maneja la visualización de los datos (del modelo). Propaga las acciones de usuario al presenter.
- Presenter: enlaza las dos capas anteriores. Sincroniza los datos mostrados en la vista con los almacenados en el modelo y actúa ante los eventos de usuario propagados por la vista.

Control de versiones
--------------------

- Herramientas consideradas: [Git](https://git-scm.com/) y [Subversion](https://subversion.apache.org/).
- Herramienta elegida: [Git](https://git-scm.com/).

Git es un sistema de control de versiones distribuido. A día de hoy, es el sistema con mayor número de usuarios. La principal diferencia con Subversion es su carácter descentralizado, que permite a cada desarrollador tener una copia en local del repositorio completo. Git está licenciado bajo la licencia de software libre GNU LGPL v2.1.

Hosting del repositorio
-----------------------

- Herramientas consideradas: [GitHub](https://github.com/), [Bitbucket](https://bitbucket.org/) y [GitLab](https://gitlab.com/).
- Herramienta elegida: [GitHub](https://github.com/).

GitHub es la plataforma web de hospedaje de repositorios por excelencia. Ofrece todas las funcionalidades de Git, revisión de código, documentación, _bug tracking_, gestión de tareas, _wikis_, red social... y numerosas integraciones con otros servicios. Es gratuita para proyectos _open source_.

Utilizamos GitHub como plataforma principal donde hospedamos el código del proyecto, la gestión de proyecto (gracias a ZenHub) y la documentación. Además, el repositorio está integrado con varios servicios de integración continua.

Gestión del proyecto
--------------------

- Herramientas consideradas: [ZenHub](https://www.zenhub.com/), [Trello](https://trello.com/), [Waffle](https://waffle.io/), [VersionOne](https://www.versionone.com/), [XP-Dev](https://xp-dev.com/) y [GitHub Projects](https://github.com/).
- Herramienta elegida: [ZenHub](https://www.zenhub.com/).

ZenHub es una herramienta de gestión de proyectos totalmente integrada en GitHub. Proporciona un tablero canvas en donde cada tarea representada se corresponde con un _issue_ nativo de GitHub. Cada tarea se puede priorizar dependiendo de su posición en la lista, se le puede asignar una estimación, uno o varios responsables y el _sprint_ al que pertenece. ZenHub también permite visualizar el gráfico _burndown_ de cada _sprint_. Es gratuita para proyectos pequeños (max. 5 colaboradores) o proyectos _open source_.

Comunicación
------------

- Herramientas consideradas: email y [Slack](https://gobees.slack.com/).
- Herramienta elegida: [Slack](https://gobees.slack.com/).

Slack es una herramienta de colaboración de equipos que ofrece salas de chat, mensajes directos y llamadas VoIP. Posee un buscador que permite encontrar todo el contenido generado dentro de Slack. Además ofrece un gran número de integraciones con otros servicios. En nuestro proyecto vamos a utilizar la integración con GitHub para crear un canal que sirva de log de todas las acciones realizadas en GitHub. Slack ofrece una versión gratuita que provee las características principales.

Entorno de desarrollo integrado (IDE)
-------------------------------------

### Java

- Herramientas consideradas: [IntelliJ IDEA](https://www.jetbrains.com/idea/) y [Eclipse](https://eclipse.org/).
- Herramienta elegida: [IntelliJ IDEA](https://www.jetbrains.com/idea/).

IntelliJ IDEA es un IDE para Java desarrollado por JetBrains. Posee un gran número de herramientas para facilitar el proceso de escritura, revisión y factorización del código. Además, permite la integración de diferentes herramientas y posee un ecosistema de _plugins_ para ampliar su funcionalidad. Su versión _community_ está disponible bajo la licencia Apache 2. Aunque también es posible adquirir la versión _Ultimate_ gratuitamente si se es estudiante.

### Android

- Herramientas consideradas: [Android Studio](https://developer.android.com/studio/index.html) y [Eclipse](https://eclipse.org/).
- Herramienta elegida: [Android Studio](https://developer.android.com/studio/index.html).

Android Studio es el IDE oficial para el desarrollo de aplicaciones Android. Está basado en IntelliJ IDEA de JetBrains. Proporciona soporte para Gradle, emulador, editor de _layouts_, refactorizaciones específicas de Android, herramientas Lint para detectar problemas de rendimiento, uso, compatibilidad de versión, etc. Se distribuye bajo la licencia Apache 2.

### Markdown

- Herramientas consideradas: [StackEdit](https://stackedit.io/) y [Haroopad](http://pad.haroopress.com/).
- Herramienta elegida: [Haroopad](http://pad.haroopress.com/).

Haroopad es un editor de documentos Markdown. Soporta Github Flavored Markdown y Mathematics Expression, además de contar con un gran número de extensiones. Se distribuye bajo licencia GNU GPL v3.0.

### LaTeX

- Herramientas consideradas: [ShareLaTeX](https://www.sharelatex.com/) y [Texmaker](http://www.xm1math.net/texmaker/).
- Herramienta elegida: [Texmaker](http://www.xm1math.net/texmaker/).

Texmaker es un editor gratuito y multiplataforma para LaTeX. Integra la mayoría de herramientas necesarias para la escritura de documentos en LaTeX (PdfLaTeX , BibTeX, makeindex, etx). Además, incluye corrector ortográfico, auto-completado, resaltado de sintaxis, visor de PDFs integrado, etc. Está licenciado bajo GNU GPL v2.

Documentación
-------------

- Herramientas consideradas: [Microsoft Word](https://products.office.com/es-es/word), [LibreOffice](https://es.libreoffice.org/), [LaTeX](https://www.latex-project.org/), [Markdown](http://daringfireball.net/projects/markdown/), [GitHub Wikis](https://github.com/).
- Herramienta elegida: [Markdown](http://daringfireball.net/projects/markdown/) + [LaTeX](https://www.latex-project.org/).

La documentación se ha desarrollado en Markdown para integrarla con el servicio de documentación continua [Read the Docs](https://readthedocs.org/). Una vez terminada, se ha exportado a LaTeX utilizando el conversor [Pandoc](http://pandoc.org/).

Markdown es un lenguaje de marcado ligero en texto plano que puede ser exportado a numerosos formatos como HTML o PDF. Su filosofía es que el lenguaje de marcado sea fácil de escribir y leer. Markdown es ampliamente utilizado para la escritura de archivos README, en foros como StackOverflow o en herramientas de comunicación como Slack.

LaTeX es un sistema de composición de textos que genera documentos con una alta calidad tipográfica. Es ampliamente utilizado para la generación de artículos y libros científicos, principalmente por su potencia a la hora de representar expresiones matemáticas.

Servicios de integración continua
---------------------------------

### Compilación y testeo

- Herramientas consideradas: [TravisCI](https://travis-ci.org/) y [CircleCI](https://circleci.com/).
- Herramienta elegida: [TravisCI](https://travis-ci.org/).

Travis es una plataforma de integración continua en la nube para proyectos alojados en GitHub. Permite realizar una _build_ del proyecto y testearla automáticamente cada vez que se realiza un _commit_, devolviendo un informe con los resultados. Es gratuita para proyectos _open source_.

### Cobertura de código

- Herramientas consideradas: [Coveralls](https://coveralls.io/) y [Codecov](https://codecov.io/).
- Herramienta elegida: [Codecov](https://codecov.io/).

Codecov es una herramienta que permite medir el porcentaje de código que está cubierto por un test. Además, realiza representaciones visuales de la cobertura y gráficos de su evolución. Posee una extensión de navegador para GitHub que permite visualizar por cada archivo de código que líneas están cubiertas por un test y cuáles no. Es gratuita para proyectos _open source_.

### Calidad del código

- Herramientas consideradas: [Codeclimate](https://codeclimate.com/) y [Codacy](https://www.codacy.com/).
- Herramienta elegida: [Codeclimate](https://codeclimate.com/).

Codeclimate es una herramienta que realiza revisiones de código automáticamente. Es gratuita para proyectos _open source_. En nuestro proyecto hemos activado los siguientes motores de chequeo: [checkstyle](https://docs.codeclimate.com/docs/checkstyle), [fixme](https://docs.codeclimate.com/docs/fixme), [markdownlint](https://docs.codeclimate.com/docs/markdownlint) y [pmd](https://docs.codeclimate.com/docs/pmd).

### Revisión de dependencias

- Herramientas consideradas: [VersionEye](https://www.versioneye.com/).
- Herramienta elegida: [VersionEye](https://www.versioneye.com/).

VersionEye es una herramienta que monitoriza las dependencias del proyecto y envía notificaciones cuando alguna de estas está desactualizada, es vulnerable o viola la licencia del proyecto. Posee una versión gratuita con ciertas limitaciones.

### Documentación

- Herramientas consideradas: [Read the Docs](https://readthedocs.org/).
- Herramienta elegida: [Read the Docs](https://readthedocs.org/).

Read the Docs es un servicio de documentación continua que permite crear y hospedar una página web generada a partir de los distintos ficheros Markdown de la documentación. Cada vez que se realiza un _commit_ en el repositorio se actualiza la versión hospedada. La página web posee un buscador, da soporte para diferentes versiones del proyecto y soporta internacionalización. El servicio es totalmente gratuito, sostenido por donaciones y subscripciones _Gold_.

Librerías
---------

### OpenCV

[OpenCV](www.opencv.org) es un paquete Open Source de visión artificial que contiene más de 2500 librerías de procesamiento de imagenes y visión artificial, escritas en C/C++ a bajo/medio nivel. Se distribuye gratuitamente bajo una licencia BSD desde hace más de una década. Posee una comunidad de más de 50.000 usuarios alrededor de todo el mundo y se ha descargado más de 8 millones de veces.

Aunque OpenCV está escrito en C/C++ posee wrappers para varias plataformas, entre ellas Android, en donde da soporte a las principales arquitecturas de CPU. Desde hace unos años, también soporta CUDA para el desarrollo en GPU tanto en escritorio como en móvil, aunque en esta última el soporte es todavía reducido.

### Android Support Library

La [librería de soporte de Android](https://developer.android.com/topic/libraries/support-library/) facilita algunas características que no se incluyen en el framework oficial. Proporciona compatibilidad a versiones antiguas con las últimas características, incluye elementos para la interfaz adicionales y utilidades extra.

### Google Guava

[Google Guava](https://github.com/google/guava) agrupa un conjunto de librerías comunes para Java. Proporciona utilidades básicas para tareas cotidianas, una extensión del _Java collections framework_ (JCF) y otras extensiones como programación funcional, almacenamiento en caché, objetos de rango o _hashing_.

### JUnit

[JUnit](http://junit.org/junit4/) es un _framework_ para Java utilizado para realizar pruebas unitarias.

### Mockito
[Mockito](http://mockito.org/) es un _framework_ de _mocking_ que permite crear objetos _mock_ fácilmente. Estos objetos simulan parte del comportamiento de una clase. Mockito está basado en EasyMock, mejorando su sintaxis haciendo los test más simples y fáciles de leer y con mensajes de error descriptivos.

### Espresso
[Espresso](https://google.github.io/android-testing-support-library/docs/espresso/) es un framework de _testing_ para Android incluido en la librería de soporte para _testing_ en Android. Provee una API para escribir UI test que simulen las interacciones de usuario con la app.
