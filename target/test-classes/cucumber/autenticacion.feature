# language: es
Característica: Servicio Autenticación (microservicio autenticacion del caso caso04)
  Los escenarios validan el contrato REST del microservicio alineado a sus endpoints.

  Escenario: el listado del recurso responde 200
    Dado el servicio "Autenticación" está disponible
    Cuando consulto el listado de "credenciales"
    Entonces el listado responde con código 200

  Escenario: ciclo de vida completo del recurso
    Dado un nuevo "credencial" con nombre "hola-cucumber"
    Cuando consulto el "credencial" recién creado
    Entonces el recurso tiene nombre "hola-cucumber" y código 200
    Cuando actualizo el "credencial" con nombre "cucumber-actualizado"
    Entonces el recurso queda con nombre "cucumber-actualizado" y código 200
    Cuando elimino el "credencial"
    Entonces la eliminación responde con código 204
    Y al consultar el "credencial" eliminado responde 404
