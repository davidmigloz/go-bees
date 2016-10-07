# Técnicas y herramientas

{Esta parte de la memoria tiene como objetivo presentar las técnicas metodológicas y las herramientas de desarrollo que se han utilizado para llevar a cabo el proyecto. Si se han estudiado diferentes alternativas de metodologías, herramientas, bibliotecas se puede hacer un resumen de los aspectos más destacados de cada alternativa, incluyendo comparativas entre las distintas opciones y una justificación de las elecciones realizadas.

No se pretende que este apartado se convierta en un capítulo de un libro dedicado a cada una de las alternativas, sino comentar los aspectos más destacados de cada opción, con un repaso somero a los fundamentos esenciales y referencias bibliográficas para que el lector pueda ampliar su conocimiento sobre el tema. }

## Repositorio

### Herramientas consideradas:
- GitHub
- Bitbucket
- GitLab

### Herramienta elegida:
- GitHub

GitHub es la plataforma web de hospedaje de repositorios por excelencia. Ofrece todas las funcionalidades de Git, revisión de código, documentación, bug tracking, gestión de tareas, wikis, red social... y numerosas integraciones con otros servicios.

Utilizaremos GitHub como plataforma principal donde hospedaremos el código del proyecto, la gestión de proyecto (gracias a ZenHub) y la documentación (gracias a sus wikis).

## Project management

### Herramientas consideradas:
- ZenHub
- Trello
- Waffle
- VersionOne
- XP-Dev

### Herramienta elegida:
- [ZenHub](https://www.zenhub.com/)

ZenHub es una herramienta de gestión de proyectos totalmente integrada en GitHub. Proporciona un tablero canvas en donde cada tarea representada se corresponde con un issue nativo de GitHub. Cada tarea se puede priorizar dependiendo de su posición en la lista, se le puede asignar una estimación, uno o varios responsables y el sprint al que pertenece. ZenHub también permite visualizar el gráfico burndown de cada sprint. Es gratuita para proyectos pequeños (max. 5 colaboradores) o proyectos open source.

## Comunicación

### Herramientas consideradas:
- Email
- Slack

### Herramienta elegida:
- [Slack](https://gobees.slack.com/)

Slack es una herramienta de colaboración de equipos que ofrece salas de chat, mensajes directos y llamadas voip. Posee un buscador que permite encontrar todo el contenido generado dentro de Slack. Además ofrece un gran número de integraciones con otros servicios. En nuestro proyecto vamos a utilizar la integración con GitHub para crear un canal que sirva de log de todas las acciones realizadas en GitHub. 

## IDE

### Herramientas consideradas:
- Android Studio
- Eclipse

### Herramienta elegida:
- [Android Studio](https://developer.android.com/studio/index.html)

Android Studio es el IDE oficial para el desarrollo de aplicaciones Android. Está basado en IntelliJ IDEA de JetBrains. Proporciona soporte para Gradle, emulador, editor de layouts, refactorizaciones específicas de Android, herramientas Lint para detectar problemas de rendimiento, uso, compatibilidad de versión, etc.

No se descarta el uso de algún otro IDE para la realización de prototipos fuera de la plataforma Android.

## Testing

### Herramientas elegidas:
- [Robolectric](http://robolectric.org/)
Robolectric es un framework para la realización de test unitarios que permite la ejecución de los test sin la necesidad de utilizar un emulador (lento). Ya que se encarga de gestionar el inflado de las vistas, cargar los recursos y muchas otras operaciones que permiten ejecutar los test directamente en la JVM pero como si de un dispositivo real se tratara.

- [Mockito](http://mockito.org/) 
Mockito es un framework de mocking que permite crear objetos mock fácilmente. Estos objetos simulan parte del comportamiento de una clase. Mockito está basado en EasyMock, mejorando su sintaxis haciendo los test más simples y fáciles de leer y con mensajes de error descriptivos.

- [Espresso](https://google.github.io/android-testing-support-library/docs/espresso/index.html)
Espresso es un framework de testing para Android includo en la librería de soporte para testing en Android. Provee una API para escribir UI test que simulen las interacciones de usuario con la app. 

- [Travis](https://travis-ci.org/)
Travis es una plataforma de integración continua en la nube para proyectos alojados en GitHub. Permite realizar una build del proyecto y testearla automaticamente cada vez que se realiza un commit, devolviendo un informe con los resultados. Es gratuita para proyectos open source. El soporte para Android está en versión beta (si tuviésemos algún problema se podría utilizar en su defecto [CircleCI](https://circleci.com/)).

- [Coveralls](https://coveralls.io/) 
Coveralls es una herramienta que permite medir el porcentaje de código que está cubierto por un test. Es gratuita para proyectos open source.

## Documentación

### Herramientas consideradas:
- Microsoft Word / LibreOffice / OpenOffice
- LaTeX
- GitHub Wikis

### Herramienta elegida:
- [GitHub Wikis](https://github.com/davidmigloz/go-bees/wiki)

GitHub ofrece junto al repositorio de código una sección para la creación de una wiki. En ella se pueden crear diferentes páginas. El texto se formatea siguiendo la sintaxis de Markdown. 

La documentación se irá desarrollando en la wiki y de cara a la entrega del proyecto se exportará a LaTeX.

- [LaTeX](https://www.latex-project.org/)

LaTeX es un sistema de composición de textos que genera documentos con una alta calidad tipográfica. Es ampliamente utilizado para la generación de artículos y libros científicos, principalmente por su potencia a la hora de representar expresiones matemáticas. 

## Librerías

- [OpenCV](www.opencv.org)

OpenCV es un paquete Open Source de visión artificial que contiene más de 2500 librerías de procesamiento de imagenes y visión artificial, escritas en C/C++ a bajo/medio nivel . Se distribuye gratuitamente bajo una licencia BSD desde hace más de una década. Posee una comunidad de más de 50.000 usuarios alrededor de todo el mundo y se ha descargado más de 8 millones de veces.

Aunque OpenCV está escrito en C/C++ posee wrappers para varias plataformas, entre ellas Android, en donde da soporte a las principales arquitecturas de CPU. Desde hace unos años, también soporta CUDA para el desarrollo en GPU tanto en escritorio como en móvil, aunque en esta última el soporte es todavía reducido.