package com.formacionbdi.microservicios.app.examenes.service;

import com.formacionbdi.microservicios.commons.examenes.models.entity.Asignatura;
import com.formacionbdi.microservicios.commons.examenes.models.entity.Examen;
import com.formacionbdi.microservicios.commons.service.CommonService;

import java.util.List;

public interface ExamenService extends CommonService<Examen> {

    public List<Examen> findByNombre(String term);

    public List<Asignatura> findAllAsignatura();



}
