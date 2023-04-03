package com.formacionbdi.microservicios.app.usuarios.services;


import com.formacionbdi.microservicios.commons.service.CommonService;
import com.formacionbdi.microservicios.commonsalumnos.models.entity.Alumno;

import java.util.List;

public interface AlumnoService extends CommonService<Alumno> {
    public List<Alumno> findByNombreOrApellido(String term);


}
