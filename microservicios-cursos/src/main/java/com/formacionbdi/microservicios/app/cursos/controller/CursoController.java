package com.formacionbdi.microservicios.app.cursos.controller;

import com.formacionbdi.microservicios.app.cursos.models.entity.Curso;
import com.formacionbdi.microservicios.app.cursos.services.CursoService;
import com.formacionbdi.microservicios.commons.controller.CommonController;
import com.formacionbdi.microservicios.commons.examenes.models.entity.Examen;
import com.formacionbdi.microservicios.commonsalumnos.models.entity.Alumno;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.*;
import java.util.stream.Collectors;

@RestController
public class CursoController extends CommonController<Curso, CursoService> {

    @PutMapping("/{id}")
    public ResponseEntity<?> editar(@Valid @RequestBody Curso curso, BindingResult result, @PathVariable Long id){

        if(result.hasErrors()){
            return this.validar(result);
        }

        Optional<Curso> c = this.service.findById(id);

        if(c.isEmpty()){
            return  ResponseEntity.notFound().build();
        }

        Curso curso1 = c.get();
        curso1.setNombre(curso.getNombre());

        return ResponseEntity.status(HttpStatus.CREATED).body(this.service.save(curso1));


    }

    @PutMapping("/{id}/asignar-alumnos")
    public ResponseEntity<?> asignarAlumnos(@RequestBody List<Alumno> alumnos, @PathVariable Long id){


        Optional<Curso> o = this.service.findById(id);

        if (o.isEmpty()){
            return ResponseEntity.notFound().build();
        }
        Curso curdoDb = o.get();
        alumnos.forEach(alumno->{
            curdoDb.addAlumnos(alumno);
        });
        return  ResponseEntity.status(HttpStatus.CREATED).body(this.service.save(curdoDb));
    }

    @PutMapping("/{id}/eliminar-alumno")
    public ResponseEntity<?> eliminarAlumno(@RequestBody Alumno alumno, @PathVariable Long id){
        Optional<Curso> o = this.service.findById(id);
        if (o.isEmpty()){
            return  ResponseEntity.notFound().build();

        }
        Curso cursoDb = o.get();

        cursoDb.eliminarAlumno(alumno);

        return ResponseEntity.status(HttpStatus.CREATED).body(this.service.save(cursoDb));

    }


    @GetMapping("/alumno/{id}")
    public ResponseEntity<?> buscarPorAlumnoId(@PathVariable Long id){
        Curso curso = service.findCursoByAlumnoId(id);
        if(curso !=null ){
            List<Long> examenesIds = (List<Long>) service.obtenerExamenesIdsConRespuestasAlumno(id);

            List<Examen> examenes = curso.getExamenes().stream().map(examen -> {
                if(examenesIds.contains(examen.getId())){
                    examen.setRespondido(true);
                }
                return examen;
            }).collect(Collectors.toList());
        }

        return ResponseEntity.ok(curso);
    }


    @PutMapping("/{id}/asignar-examenes")
    public ResponseEntity<?> asignarExamenes(@RequestBody List<Examen> examenes, @PathVariable Long id){
        Optional<Curso> o = this.service.findById(id);

        if (o.isEmpty()){
            return ResponseEntity.notFound().build();
        }
        Curso curdoDb = o.get();
        examenes.forEach(examen->{
            curdoDb.addExamen(examen);
        });
        return  ResponseEntity.status(HttpStatus.CREATED).body(this.service.save(curdoDb));
    }


    @PutMapping("/{id}/eliminar-examen")
    public ResponseEntity<?> eliminarExamen(@RequestBody Examen examen, @PathVariable Long id){
        Optional<Curso> o = this.service.findById(id);
        if (o.isEmpty()){
            return  ResponseEntity.notFound().build();

        }
        Curso cursoDb = o.get();

        cursoDb.removeExamen(examen);

        return ResponseEntity.status(HttpStatus.CREATED).body(this.service.save(cursoDb));

    }





    //testear el balanceador de carga
    //esto es solamente para probar el balanceador de carga

    @Value("${config.balanceador.test}")
    private String balanceadorTest;


    @GetMapping("/balanceador-test")
    public ResponseEntity<?> balanceadorTest(){
        Map<String, Object> response = new HashMap<String, Object>();
        response.put("balanceador", balanceadorTest);
        response.put("cursos", service.findAll());
        return ResponseEntity.ok(response);
    }






}
