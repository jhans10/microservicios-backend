package com.formacionbdi.microservicios.commons.controller;

import com.formacionbdi.microservicios.commons.service.CommonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
public class CommonController<E, S extends  CommonService<E>> {

    @Autowired
   protected S service;

    @GetMapping
    public ResponseEntity<?> listar(){
        return ResponseEntity.ok().body(service.findAll());
    }

    @GetMapping("/pagina")
    public ResponseEntity<?> listar(Pageable pageable){
        return ResponseEntity.ok().body(service.findAll(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable Long id){
        Optional<E> o = service.findById(id);
        if(o.isEmpty()){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().body(o.get());
    }

    @PostMapping
    public ResponseEntity<?>  crear(@Valid @RequestBody E entity, BindingResult result){

        if(result.hasErrors()){
            return this.validar(result);
        }

        E entity1 = service.save(entity);
        return ResponseEntity.status(HttpStatus.CREATED).body(entity1);

    }


    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Long id){
        service.deleteById(id);
       return ResponseEntity.noContent().build();
    }

    protected ResponseEntity<?> validar(BindingResult result){
        Map<String, Object> errores = new HashMap<>();
        result.getFieldErrors().forEach(err->{
            errores.put(err.getField(), " el campo "+err.getField()+" "+err.getDefaultMessage());
        });
        return ResponseEntity.badRequest().body(errores);
    }








}
