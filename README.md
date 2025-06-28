Para comenzar este proyecto, he seguido un tutorial creado por las siguientes personas:

- Felipe Garcia (@fgarciafer)
- Armen Mirzoyan (@armirzoya)
- Carlos Aguilar (@caaguila)
- Jhonatan Core (@corevill)
- Carlos Navarro (@DarkWarlord)
- Cesar Cardona (@Cazs03)
- Marina Valls (@mvalemany)
- Jaume Segarra (@jaumesegarra)
- Laura Medina (@larulirea)
- Yolanda Ubeda
- Pablo Jimenez (@pajimene)

Posteriormente, he añadido algunas funcionalidades que creo que debería de tener una aplicación así.

OBSERVACIONES:

- He añadido algunas funcionalidades más para así poder darle mi toque personal si quiero añadirlo a mi portfolio
  y así ampliar más aún el campo de aprendizaje. He intentado cumplir las best practices de lo que he implementado.

BACK-END:

1. He utilizado una clase custom StatusResponse (com.ccsq.tutorial.dto) para estandarizar las respuestas que se envían
   al cliente. La idea de utilizar esta clase ha surgido a raíz de la necesidad de comunicar al front exactamente
   qué errores hay o qué acción se ha completado. ¿Es correcto este enfoque?
2. Respecto al punto 1, he ido haciendo catch en cada Controller de las excepciones (además de la propia respuesta
   StatusResponse del Service), pero en el Controller a nivel más general. He encontrado que la
   best practice era utilizar un Controller General que redirigiese a cada Controller individual. Me gustaría recibir 
   recomendaciones sobre las buenas prácticas en este sentido, si fuese posible. ¡Gracias!
3. Restricción añadida: no se pueden introducir objetos en la base de datos si uno de los campos no se ha informado.

Bugs conocidos:

*Todavía ninguno, por suerte.*

FRONT-END:

1. Añadida funcionalidad para enviar los formularios al pulsar 'Enter' con el focus en cualquiera de los inputs de campo.
2. Añadida la funcionalidad de filtrar los 'Game' pulsando 'Enter' con el focus en el input de filtrado.
3. Añadida alerta dinámica que aparece después de cada operación para proporcionar retroacción al usuario.

Bugs conocidos:

1. Si se crea/borra/modifica muy seguido, los 'alerts' se fusionan y el texto queda ilegible.