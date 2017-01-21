Especificación de diseño
========================

Introducción
------------

En este anexo se define cómo se han resulto los objetivos y
especificaciones expuestos con anterioridad. Define los datos que va a
manejar la aplicación, su arquitectura, el diseño de sus interfaces, sus
detalles procedimentales, etc.

Diseño de datos
---------------

La aplicación cuenta con las siguientes entidades:

-  **Colmenar (Apiary)**: tiene un nombre, una imagen, una localización
   y unas notas. A su vez, guarda un registro del tiempo meteorológico
   actual y varios registros del tiempo que hacía cuando se realizaron
   las grabaciones de sus colmenas.

-  **Colmena (Hive)**: tiene un nombre, una imagen y unas notas. A su
   vez, posee varias grabaciones de distintas monitorizaciones de la
   colmena.

-  **Registro (Record)**: se corresponde a la salida del algoritmo de
   conteo al analizar un fotograma. Tiene un *timestamp* y el número de
   abejas que había en el fotograma.

-  **Registro meteorológico (MeteoRecord)**: guarda información sobre el
   estado meteorológico en una localización y un momento dado. Tiene un
   *timestamp*, la localidad, el código correspondiente a la condición
   meteorológica, el icono correspondiente, temperatura, presión,
   humedad, velocidad y dirección del viento, porcentaje de nubes,
   precipitaciones, y nieve.

Diagrama E/R
~~~~~~~~~~~~

|er|

.. |er| image:: ../../img/er-diagram.png


Diagrama Relacional
~~~~~~~~~~~~~~~~~~~

|relational|

.. |relational| image:: ../../img/relational-diagram.png

Diseño procedimental
--------------------

Diseño arquitectónico
---------------------
