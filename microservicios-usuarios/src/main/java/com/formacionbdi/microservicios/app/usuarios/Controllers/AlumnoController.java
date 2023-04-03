package com.formacionbdi.microservicios.app.usuarios.Controllers;

import com.formacionbdi.microservicios.app.usuarios.services.AlumnoService;
import com.formacionbdi.microservicios.commons.controller.CommonController;
import com.formacionbdi.microservicios.commonsalumnos.models.entity.Alumno;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.util.Optional;

@RestController
public class AlumnoController extends CommonController<Alumno, AlumnoService> {


    @PutMapping("/{id}")
    public ResponseEntity<?> editar(@Valid @RequestBody Alumno alumno, BindingResult result, @PathVariable Long id) {

        if (result.hasErrors()) {
            return this.validar(result);
        }


        Optional<Alumno> o = service.findById(id);
        if (o.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Alumno alumnoActualizado = o.get();
        alumnoActualizado.setNombre(alumno.getNombre());
        alumnoActualizado.setApellido(alumno.getApellido());
        alumnoActualizado.setEmail(alumno.getEmail());
        return ResponseEntity.status(HttpStatus.CREATED).body(service.save(alumnoActualizado));


    }

    @GetMapping("/filtrar/{term}")
    public ResponseEntity<?> filtrar(@PathVariable String term) {
        return ResponseEntity.ok(service.findByNombreOrApellido(term));
    }

    //metodo crear con foto
    @PostMapping("/crear-con-foto")
    public ResponseEntity<?> crearConFoto(@Valid Alumno alumno, BindingResult result, @RequestParam MultipartFile archivo) throws IOException {
        if (!archivo.isEmpty()) {

            alumno.setFoto(archivo.getBytes());
        }

        return super.crear(alumno, result);
    }

    //editar con foto
    @PutMapping("/editar-con-foto/{id}")
    public ResponseEntity<?> editarConFoto(@Valid Alumno alumno, BindingResult result, @PathVariable Long id, @RequestParam MultipartFile archivo) throws IOException {

        if (result.hasErrors()) {
            return this.validar(result);
        }


        Optional<Alumno> o = service.findById(id);
        if (o.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Alumno alumnoActualizado = o.get();
        alumnoActualizado.setNombre(alumno.getNombre());
        alumnoActualizado.setApellido(alumno.getApellido());
        alumnoActualizado.setEmail(alumno.getEmail());
        if (!archivo.isEmpty()) {

            alumnoActualizado.setFoto(archivo.getBytes());
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(service.save(alumnoActualizado));


    }

    //ver foto
    @GetMapping("/uploads/img/{id}")
    public ResponseEntity<?> verFoto(@PathVariable Long id) {
        Optional<Alumno> o = service.findById(id);

        if (o.isEmpty() || o.get().getFoto() == null) {

            return ResponseEntity.notFound().build();

        }
        Resource image = new ByteArrayResource(o.get().getFoto());

        return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(image);
    }
}