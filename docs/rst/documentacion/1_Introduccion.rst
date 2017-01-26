Introducción
============

Las abejas son una pieza clave en nuestro ecosistema. La producción de
alimentos a nivel mundial y la biodiversidad de nuestro planeta dependen
en gran medida de ellas. Son las encargadas de polinizar el 70% de los
cultivos de comida, que suponen un 90% de la alimentación humana [art:bees_decline]_.
Sin embargo, la población mundial de abejas está disminuyendo a pasos
agigantados en los últimos años debido, entre otras causas, al uso
extendido de plaguicidas tóxicos, parásitos como la varroa o la
expansión del avispón asiático [art:ccd]_.

Actualmente los apicultores inspeccionan sus colmenares de forma manual.
Uno de los indicadores que más información les proporciona es la
actividad de vuelo de la colmena (número de abejas en vuelo a la entrada
de la colmena en un determinado instante) [art:campbell2008]_. Este dato, junto con
información previa de la colmena y conocimiento de las condiciones
locales, permite conocer al apicultor el estado de la colmena con
bastante seguridad, pudiendo determinar si esta necesita o no una
intervención.

La actividad de vuelo de una colmena varía dependiendo de múltiples
factores, tanto externos como internos a la colmena. Entre ellos se
encuentran la propia población de la colmena, las condiciones
meteorológicas, la presencia de enfermedades, parásitos o depredadores,
la exposición a tóxicos, la presencia de fuentes de néctar, etc. A pesar 
de los numerosos factores que influyen en la actividad de vuelo, su 
conocimiento es de gran ayuda para la toma de decisiones por parte del 
apicultor. Ya que este posee información sobre la mayoría de los 
factores necesarios para su interpretación.

La captación prolongada de la actividad de vuelo de forma manual es muy
costosa, tediosa y puede introducir una tasa de error elevada. Es por
esto que a lo largo de los años se haya intentado automatizar este
proceso de muy diversas maneras. Los primeros intentos se remontan a
principios del siglo XX, donde se desarrollaron contadores por contacto
eléctrico [art:lundie1925]_. Otros métodos posteriores se basan en sensores de
infrarrojos [art:struye1994]_, sensores capacitivos [art:campbell2005]_, códigos de barras [beebarcode]_ o
incluso en microchips acoplados a las abejas [art:decourtye_honeybee_2011]_. En los últimos años,
se han desarrollado numerosos métodos basados en visión artificial
[art:campbell2008]_ [art:chiron2013a]_ [art:chiron2013]_ [art:tashakkori2015]_.

Los métodos basados en contacto, sensores de infrarrojos o capacitivos
tienen el inconveniente de que es necesario realizar modificaciones en
la colmena, mientras que en los basados en códigos de barras o
microchips es necesario manipular las abejas directamente. Estos motivos
los convierten en métodos poco prácticos fuera del campo investigador.
Por el contrario, la visión artificial aporta un gran potencial, ya que
evita tener que realizar ningún tipo de modificación ni en el entorno,
ni en las abejas. Además, abre la puerta a la monitorización de nuevos
parámetros como la detección de enjambrazón (división de la colmena y salida de un enjambre) o la detección de amenazas (avispones, abejaruco, etc.).

Todos los métodos basados en visión artificial propuestos hasta la fecha
utilizan hardware específico con un coste elevado. En este trabajo se
propone un método de monitorización de la actividad de vuelo de una
colmena mediante la cámara de un *smartphone* con Android.

El método propuesto podría revolucionar el campo de la monitorización de
la actividad de vuelo de colmenas, ya que lo hace accesible al gran público. Ya no es necesario
contar con costoso hardware, difícil de instalar. Solamente es necesario
disponer de un *smartphone* con cámara y la aplicación GoBees. Además,
esta facilita la interpretación de los datos, representándolos
gráficamente y añadiendo información adicional como la situación
meteorológica. Permitiendo a los apicultores centrar su atención donde
realmente es necesaria.

Estructura de la memoria
------------------------

La memoria sigue la siguiente estructura:

-  **Introducción:** breve descripción del problema a resolver y la
   solución propuesta. Estructura de la memoria y listado de materiales
   adjuntos.
-  **Objetivos del proyecto:** exposición de los objetivos que persigue
   el proyecto.
-  **Conceptos teóricos:** breve explicación de los conceptos teóricos
   clave para la comprensión de la solución propuesta.
-  **Técnicas y herramientas:** listado de técnicas metodológicas y
   herramientas utilizadas para gestión y desarrollo del proyecto.
-  **Aspectos relevantes del desarrollo:** exposición de aspectos
   destacables que tuvieron lugar durante la realización del proyecto.
-  **Trabajos relacionados:** estado del arte en el campo de la
   monitorización de la actividad de vuelo de colmenas y proyectos
   relacionados.
-  **Conclusiones y líneas de trabajo futuras:** conclusiones obtenidas
   tras la realización del proyecto y posibilidades de mejora o
   expansión de la solución aportada.

Junto a la memoria se proporcionan los siguientes anexos:

-  **Plan del proyecto software:** planificación temporal y estudio de
   viabilidad del proyecto.
-  **Especificación de requisitos del software:** se describe la fase de
   análisis; los objetivos generales, el catálogo de requisitos del
   sistema y la especificación de requisitos funcionales y no
   funcionales.
-  **Especificación de diseño:** se describe la fase de diseño; el
   ámbito del software, el diseño de datos, el diseño procedimental y el
   diseño arquitectónico.
-  **Manual del programador:** recoge los aspectos más relevantes
   relacionados con el código fuente (estructura, compilación,
   instalación, ejecución, pruebas, etc.).
-  **Manual de usuario:** guía de usuario para el correcto manejo de la
   aplicación.

Materiales adjuntos
-------------------

Los materiales que se adjuntan con la memoria son:

- Aplicación para Android GoBees.
- Aplicación Java para el desarrollo del algoritmo.
- Aplicación Java para el etiquetado de fotogramas.
- JavaDoc.
- *Dataset* de vídeos de prueba.

Además, los siguientes recursos están accesibles a través de internet:

- Página web del proyecto: http://gobees.io/
- GoBees en Google Play: https://play.google.com/store/apps/details?id=com.davidmiguel.gobees
- Repositorio del proyecto: https://github.com/davidmigloz/go-bees
- Repositorio de las herramientas desarrolladas para el proyecto: https://github.com/davidmigloz/go-bees-prototypes

.. References

.. [art:bees_decline]
   http://www.greenpeace.org/switzerland/Global/international/publications/agriculture/2013/BeesInDecline.pdf
.. [art:ccd]
   https://agresearchmag.ars.usda.gov/AR/archive/2008/May/colony0508.pdf
.. [art:campbell2008]
   http://homepages.inf.ed.ac.uk/rbf/VAIB08PAPERS/vaib9\_mummert.pdf
.. [art:lundie1925]
   https://archive.org/details/flightactivities1328lund
.. [art:struye1994]
   https://hal.archives-ouvertes.fr/hal-00891170/document
.. [art:campbell2005]
   http://stacks.iop.org/0957-0233/16/i=12/a=015
.. [beebarcode]
   http://www.uprintlabels.com/barcode-labels-uses.html
.. [art:decourtye_honeybee_2011]
   https://www.ncbi.nlm.nih.gov/pubmed/21267650
.. [art:chiron2013a]
   http://link.springer.com/chapter/10.1007%2F978-3-642-41181-6\_71
.. [art:chiron2013]
   http://jivp.eurasipjournals.springeropen.com/articles/10.1186/1687-5281-2013-59
.. [art:tashakkori2015]
   http://ieeexplore.ieee.org/lpdocs/epic03/wrapper.htm?arnumber=7133029
