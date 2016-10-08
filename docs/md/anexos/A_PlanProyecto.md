Plan del proyecto software
==========================

Introducción
------------

La fase de planificación es un punto clave en cualquier proyecto. En esta fase se estima el trabajo, el tiempo y el dinero que va a suponer la realización del proyecto. Para ello, se analiza minuciosamente cada una de las partes que componen el proyecto. Este análisis, además de permitir conocer los recursos necesarios, es de gran ayuda en fases posteriores del desarrollo. En este anexo se detalla todo este proceso.

La fase de planificación se puede dividir a su vez en:

- Planificación temporal.
- Estudio de viabilidad.

En la primera parte, se elabora un calendario o un programa de tiempos. En estos se estima el tiempo necesario para la realización de cada una de las partes del proyecto. Se debe establecer una fecha fija de inicio del proyecto y una fecha de finalización estimada. Teniendo en cuenta el peso de cada una de las tareas y los requisitos que se deben cumplir para poder empezar a trabajar en cada una de ellas.

La segunda parte se centra en la viabilidad del proyecto. El estudio de viabilidad se puede dividir a su vez en dos apartados:

- Viabilidad económica: donde se estiman los costes y los beneficios que puede suponer la realización del proyecto.
- Viabilidad legal: el contexto en el que se ejecuta el proyecto está regulado por una serie de leyes. Se deben analizar todas aquellas que afecten al proyecto. En el caso del software, las licencias y la Ley de Protección de Datos pueden ser los temas más relevantes.

Planificación temporal
----------------------

Al inicio del proyecto se planteó utilizar una metodología ágil como Scrum para la gestión del proyecto. Aunque no se ha seguido al 100% la metodología al tratarse de un proyecto educativo (no éramos un equipo de 4 a 8 personas, no hubo reuniones diarias, etc.), sí que se ha aplicado en líneas generales una filosofía ágil:

- Se aplicó una estrategia de desarrollo incremental a través de iteraciones (sprints) y revisiones.
- La duración media de los sprints fue de una semana.
- Al finalizar cada sprint se entregaba una parte del producto operativa (incremento).
- Se realizaban reuniones de revisión al finalizar cada sprint y al mismo tiempo de planificación del nuevo sprint.
- En la planificación del sprint se generaba una pila de tareas a realizar.
- Estas tareas se estimaban y priorizaban en un tablero canvas.
- Para monitorizar el progreso del proyecto se utilizó gráficos burndown.

Comentar que la estimación se realizó mediante los story points que provee ZenHub y, a su vez, se les asignó una estimación temporal (cota superior) que se recoge en la siguiente tabla:

| Story points | Estimación temporal |
|----|----------|
| 1  | 15min    |
| 2  | 45min    |
| 3  | 2h       |
| 5  | 5h       |
| 8  | 12h      |
| 13 | 24h      |
| 21 | 2,5 días |
| 40 | 1 semana |


A continuación se describen los diferentes sprints que se han realizado.

### Sprint 0 (09/09/16 - 16/09/16)

La reunión de planificación de este sprint marcó el comienzo del proyecto. En una reunión previa se había plateado la idea del proyecto al tutor y este la había aceptado. En esta nueva reunión se profundizó en la idea, se formó el equipo del proyecto y se plantearon los objetivos del primer sprint.

Los objetivos fueron: profundizar y formalizar los objetivos del proyecto, investigar el estado del arte en algoritmos de detección y tracking aplicados a la apicultura, establecer el conjunto de herramientas que conformarían el entorno de desarrollo, la gestión del proyecto y la comunicación del equipo y, por último, realizar un esquema rápido de la aplicación que se deseaba desarrollar.

Las tareas en las que se descompusieron los objetivos se pueden ver en: [Sprint 0](https://github.com/davidmigloz/go-bees/milestone/1?closed=1).

Se estimaron 8 horas de trabajo y se invirtieron finalmente 9,25 horas, completando todas las tareas.

![Sprint 0](../../img/burndowns/sprint0.png)

### Sprint 1 (17/09/16 - 23/09/16)

Los objetivos de este sprint fueron: tomar contacto con OpenCV para Android, realizar un curso online de iniciación a Android, investigar qué algoritmos de detección y tracking estaban disponibles en OpenCV para Android y empezar a trabajar en la documentación.

En este sprint se tuvo la suerte de hablar sobre el proyecto con Rafael Saracchini, investigador en temas de visión artificial en el Instituto Tecnológico de Castilla y León. Rafael nos propuso una serie de algoritmos que nos podían ser útiles y otros que no funcionarían bajo nuestros requisitos.

Las tareas en las que se descompusieron los objetivos se pueden ver en: [Sprint 1](https://github.com/davidmigloz/go-bees/milestone/2?closed=1).

Se estimaron 7,25 horas de trabajo y se invirtieron finalmente 13,25 horas, completando todas las tareas.

![Sprint 1](../../img/burndowns/sprint1.png)

### Sprint 2 (24/09/16 - 29/09/16)

Los objetivos de este sprint fueron: investigar cómo implementar con OpenCV los algoritmos descritos en el sprint anterior, continuar la formación en Android y OpenCV y realizar grabaciones en el colmenar para tener un conjunto de vídeos con los que realizar pruebas.

Las tareas en las que se descompusieron los objetivos se pueden ver en: [Sprint 2](https://github.com/davidmigloz/go-bees/milestone/3?closed=1).

Mientras se realizaba una de las tareas del sprint, se encontraron dos bugs relacionados con OpenCV y Android ([#26](https://github.com/davidmigloz/go-bees/issues/26) y [#27](https://github.com/davidmigloz/go-bees/issues/27)) que nos impidieron continuar el desarrollo. El investigar su origen y buscar soluciones supuso una gran cantidad de horas y no se lograron resolver hasta el siguiente sprint.

Se estimaron 11,75 horas de trabajo y se invirtieron finalmente 33 horas, quedando dos tareas pendientes para terminar durante el siguiente sprint.

![Sprint 2](../../img/burndowns/sprint2.png)

### Sprint 3 (30/09/16 - 06/10/16)

Los objetivos de este sprint fueron: intentar resolver los bugs descubiertos en el sprint anterior, o si esto fuese imposible, buscar una vía alternativa para continuar el proyecto y continuar investigando las implementaciones de los algoritmos de extracción de fondo en OpenCV.

Las tareas en las que se descompusieron los objetivos se pueden ver en: [Sprint 3](https://github.com/davidmigloz/go-bees/milestone/4?closed=1).

Se estimaron 20,75 horas de trabajo y se invirtieron finalmente 31 horas, quedando una tarea por terminar.

![Sprint 3](../../img/burndowns/sprint3.png)



Estudio de viabilidad
---------------------

## Viabilidad económica

## Viabilidad legal