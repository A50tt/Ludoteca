Project done with a Capgemini tutorial for training purposes.

This part represents de BACKEND of the application.

Credit to the creators of the tutorial I followed:

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

OBSERVACIONES:

He añadido algunas funcionalidades más allá para así poder darle mi toque personal si quiero añadirlo a mi portfolio
y así también ampliar más aún el campo de aprendizaje.

BACK-END:

1. He utilizado una clase custom StatusResponse (com.ccsq.tutorial.dto) para estandarizar las respuestas que se envían
   al cliente. La idea de utilizar esta clase custom ha surgido a raíz de la necesidad de comunicar al front exactamente
   qué error hay en la validación de Loan, para resaltar el componente específico de la Form. ¿Es correcto este enfoque?
   La verdad, personalmente no había trabajado nunca con arquitecturas servidor-cliente, me ha gustado.
2. Restricción añadida: no se pueden introducir objetos en la base de datos si uno de los campos no se ha informado.

Bugs conocidos:

*Todavía ninguno, por suerte.*

FRONT-END:

1. Añadida funcionalidad para enviar los formularios al pulsar Enter con el focus en cualquiera de los inputs de campo.
2. Añadida alerta dinámica en pantalla después de cada operación para proporcionar retroacción.

Bugs conocidos:

1. Si se crea/borra/modifica muy seguido, los alerts se fusionan y el texto es ilegible.