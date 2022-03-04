package com.valido.server.service;

import com.valido.server.model.Server;

import java.io.IOException;
import java.util.Collection;

public interface ServerService {
    /**
     * Crea un nuevo objeto Server y lo almacena en la bbdd
     * @param server El objeto que va a guardar
     * @return El objeto creado
     */
    Server create(Server server);

    /**
     * Hace ping a la ip recibida para comprobar el status
     * @param ipAddress IP a la que hacer ping
     * @return El objeto servidor para actualizar el status en la UI
     * @throws IOException si el servidor no es alcanzable
     */
    Server ping(String ipAddress) throws IOException;

    /**
     * Devuelve una lista de servidores con el límite indicado
     * @param limit Límite de objetos que queremos recibir
     * @return Lista de Servidores
     */
    Collection<Server> list(int limit);

    /**
     * Recupera el Servidor según el Id indicado
     * @param id Id del servidor a recuperar
     * @return
     */
    Server get(Long id);

    /**
     * Actualiza la información del servidor
     * @param server Servidor a actualizar
     * @return Servidor con info actualizada
     */
    Server update(Server server);

    /**
     * Borra un servidor de la bbdd
     *
     * @param id El id del servidor a borrar
     * @return True si se ha borrado y false si no
     */
    public Boolean delete(Long id);
}
