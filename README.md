Project done with Capgemini Tutorials for training purposes. This part represents de BACKEND of the application.

Credit to the creators:

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

1. He utilizado una clase StatusResponse (com.ccsq.tutorial.dto) para estandarizar las respuestas. La idea ha surgido a
   raíz de la necesidad de comunicar al front exactamente qué error hay en la validación de Loan, para resaltar el
   componente específico de la Form. ¿Es correcto este enfoque?
2. Añadida alerta general después de cada operación que da al usuario retroacción.
BUGS CONOCIDOS:
1. Si se crea/borra/modifica seguido, los alerts se fusionan y no queda estético.