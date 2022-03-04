package com.valido.server.service.implementation;

import com.valido.server.enumeration.Status;
import com.valido.server.model.Server;
import com.valido.server.repo.ServerRepo;
import com.valido.server.service.ServerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.transaction.Transactional;
import java.io.IOException;
import java.net.InetAddress;
import java.util.Collection;
import java.util.Random;

@RequiredArgsConstructor
@Service
@Transactional
@Slf4j
public class ServerServiceImplementation implements ServerService {

    private final ServerRepo serverRepo;

    /**
     * Crea un nuevo objeto Server y lo almacena en la bbdd
     *
     * @param server El objeto que va a guardar
     * @return El objeto creado
     */
    @Override
    public Server create(Server server) {
        //Informamos por el log (existente gracias a la anotación Slf4j)
        log.info("Saving new server: {}", server.getName());

        //Seteamos la imagen del servidor
        server.setImageUrl(setServerImageUrl());

        //Usamos el JpaRepository para almacenar datos en la bbdd
        return serverRepo.save(server);
    }

    /**
     * Hace ping a la ip recibida para comprobar el status
     *
     * @param ipAddress IP a la que hacer ping
     * @return El objeto servidor para actualizar el status en la UI
     * @throws IOException si el servidor no es alcanzable
     */
    @Override
    public Server ping(String ipAddress) throws IOException {
        //Informamos por el log (existente gracias a la anotación Slf4j)
        log.info("Pinging server IP: {}", ipAddress);

        //Recuperamos el servidor al que queremos hacer ping
        Server server = serverRepo.findByIpAddress(ipAddress);

        InetAddress address = InetAddress.getByName(ipAddress);

        server.setStatus(address.isReachable(10000) ? Status.SERVER_UP : Status.SERVER_DOWN);

        serverRepo.save(server);

        return server;
    }

    /**
     * Devuelve una lista de servidores con el límite indicado
     *
     * @param limit Límite de objetos que queremos recibir
     * @return Lista de Servidores
     */
    @Override
    public Collection<Server> list(int limit) {
        log.info("Fetching all servers");
        return serverRepo.findAll(PageRequest.of(0, limit)).toList();
    }

    /**
     * Recupera el Servidor según el Id indicado
     *
     * @param id Id del servidor a recuperar
     * @return
     */
    @Override
    public Server get(Long id) {
        log.info("Fetching server by id: {}", id);
        return serverRepo.findById(id).get();
    }

    /**
     * Actualiza la información del servidor
     *
     * @param server Servidor a actualizar
     * @return Servidor con info actualizada
     */
    @Override
    public Server update(Server server) {
        log.info("Updating server: {}", server.getName());
        return serverRepo.save(server);
    }

    /**
     * Borra un servidor de la bbdd
     *
     * @param id El id del servidor a borrar
     * @return True si se ha borrado y false si no
     */
    @Override
    public Boolean delete(Long id) {
        log.info("Deleting server: {}", id);
        serverRepo.deleteById(id);
        return Boolean.TRUE;
    }

    /**
     * Asigna una imagen aleatoria para el server
     * @return La ruta de la imagen
     */
    private String setServerImageUrl() {
        String[] imageNames = {"server1.png", "server2.png", "server3.png", "server4.png"};
        return ServletUriComponentsBuilder.fromCurrentContextPath().path("/server/image/" + imageNames[new Random().nextInt(4)]).toUriString();
    }
}
