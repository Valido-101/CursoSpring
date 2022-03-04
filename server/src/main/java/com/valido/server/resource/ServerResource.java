package com.valido.server.resource;

import com.valido.server.enumeration.Status;
import com.valido.server.model.Response;
import com.valido.server.model.Server;
import com.valido.server.service.implementation.ServerServiceImplementation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.Map;

import static com.valido.server.enumeration.Status.SERVER_UP;
import static java.time.LocalDateTime.now;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.IMAGE_PNG_VALUE;

@RestController
@RequestMapping("/server")
@RequiredArgsConstructor
public class ServerResource {

    private final ServerServiceImplementation serverService;

    /**
     * Devuelve la lista de servidores que hay almacenados
     * @return Response con la lista de servidores
     */
    @GetMapping("/list")
    public ResponseEntity<Response> getServers(){
        return ResponseEntity.ok(
                Response.builder()
                        .timeStamp(now())
                        .data(Map.of("servers", serverService.list(30)))
                        .message("Servidores recuperados")
                        .status(OK)
                        .statusCode(OK.value())
                        .build()
        );
    }

    /**
     * Hace ping al servidor indicado
     * @param ipAddress Dirección IP del servidor al que hacemos ping
     * @return Respuesta con mensaje indicando si se ha podido hacer o no
     * @throws IOException
     */
    @GetMapping("/ping/{ipAddress}")
    public ResponseEntity<Response> pingServer(@PathVariable("ipAddress") String ipAddress) throws IOException {
        Server server = serverService.ping(ipAddress);

        return ResponseEntity.ok(
                Response.builder()
                        .timeStamp(now())
                        .data(Map.of("server", server))
                        .message(server.getStatus() == SERVER_UP ? "Ping exitoso" : "Ping fallido")
                        .status(OK)
                        .statusCode(OK.value())
                        .build()
        );
    }

    /**
     * Almnacena el servidor pasado por parámetro en la bbdd
     * @param server El servidor a guardar
     * @return Respuesta con el servidor creado
     */
    @PostMapping("/save")
    public ResponseEntity<Response> saveServer(@RequestBody @Valid Server server) {


        return ResponseEntity.ok(
                Response.builder()
                        .timeStamp(now())
                        .data(Map.of("server", serverService.create(server)))
                        .message("Servidor creado")
                        .status(CREATED)
                        .statusCode(CREATED.value())
                        .build()
        );
    }

    /**
     * Recupera un servidor indicado por parámetro
     * @param id El id del servidor a recuperar
     * @return Respuesta con el servidor recuperado
     */
    @GetMapping("/get/{id}")
    public ResponseEntity<Response> getServer(@PathVariable("id") Long id) {
        return ResponseEntity.ok(
                Response.builder()
                        .timeStamp(now())
                        .data(Map.of("server", serverService.get(id)))
                        .message("Servidor recuperado")
                        .status(OK)
                        .statusCode(OK.value())
                        .build()
        );
    }

    /**
     * Borra un servidor indicado por parámetro
     * @param id El id del servidor a borrar
     * @return Respuesta con el servidor recuperado
     */
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Response> deleteServer(@PathVariable("id") Long id) {
        return ResponseEntity.ok(
                Response.builder()
                        .timeStamp(now())
                        .data(Map.of("deleted", serverService.delete(id)))
                        .message("Servidor borrado")
                        .status(OK)
                        .statusCode(OK.value())
                        .build()
        );
    }

    /**
     * Recupera una imagen del servidor
     * @param fileName El nombre del archivo
     * @return El archivo en array de bytes
     * @throws IOException
     */
    @GetMapping(path = "/image/{fileName}", produces = IMAGE_PNG_VALUE)
    public byte[] getServerImage(@PathVariable("fileName") String fileName) throws IOException {
        return Files.readAllBytes(Paths.get(System.getProperty("user.home") + "Downloads/images/" + fileName));
    }
}
