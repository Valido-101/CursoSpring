package com.valido.server.repo;

import com.valido.server.model.Server;
import org.springframework.data.jpa.repository.JpaRepository;

//Hereda de JpaRepository, al que hay que pasarle el tipo de dato (en este caso la clase Server) y el tipo de id,
//que en la clase Server es long
public interface ServerRepo extends JpaRepository<Server, Long> {
    Server findByIpAddress(String ipAddress);
}
