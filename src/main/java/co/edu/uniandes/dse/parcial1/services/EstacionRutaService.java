package co.edu.uniandes.dse.parcial1.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import co.edu.uniandes.dse.parcial1.entities.EstacionEntity;
import co.edu.uniandes.dse.parcial1.entities.RutaEntity;
import co.edu.uniandes.dse.parcial1.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.parcial1.exceptions.IllegalOperationException;
import co.edu.uniandes.dse.parcial1.repositories.EstacionRepository;
import co.edu.uniandes.dse.parcial1.repositories.RutaRepository;
import org.springframework.transaction.annotation.Transactional;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class EstacionRutaService {

    @Autowired
    private RutaRepository rutaRepository;

    @Autowired
    private EstacionRepository estacionRepository;

    @Transactional
    public EstacionEntity addEstacionRuta(Long id_estacion, Long id_ruta) throws EntityNotFoundException, IllegalOperationException{
        Optional<EstacionEntity> estacion = estacionRepository.findById(id_estacion);
        if (estacion.isEmpty())
            throw new EntityNotFoundException("No se ha encontrado la estación...");
        Optional<RutaEntity> ruta = rutaRepository.findById(id_ruta);
        if (ruta.isEmpty())
            throw new EntityNotFoundException("No se ha encontrado la ruta buscada...");
        for (EstacionEntity e: ruta.get().getEstaciones()){
            if (e.equals(estacion.get())){
                throw new IllegalOperationException("Esta estacion ya existe en la ruta...");
            }
        }
        estacion.get().getRutas().add(ruta.get());
        return estacion.get();
    }

    @Transactional
    public void removeEstacionRuta(Long id_estacion, Long id_ruta) throws EntityNotFoundException, IllegalOperationException{
        Optional<EstacionEntity> estacion = estacionRepository.findById(id_estacion);
        if (estacion.isEmpty())
            throw new EntityNotFoundException("No se ha encontrado la estación...");
        Optional<RutaEntity> ruta = rutaRepository.findById(id_ruta);
        if (ruta.isEmpty())
            throw new EntityNotFoundException("No se ha encontrado la ruta buscada...");
        Boolean existe = false;
        for (EstacionEntity e: ruta.get().getEstaciones()){
            if (e.equals(estacion.get())){
                existe = true;
            }
        }
        if (existe){
            estacion.get().getRutas().remove(ruta.get());
        }
        else{
            throw new IllegalOperationException("Esta estación no existe en la ruta dada...");
        }
    }
    

    @Transactional
    public List<RutaEntity> getRutas(Long id_estacion, Long id_ruta) throws EntityNotFoundException {
        Optional<EstacionEntity> estacion = estacionRepository.findById(id_estacion);
        if (estacion.isEmpty())
            throw new EntityNotFoundException("No se ha encontrado la estación...");
        Optional<RutaEntity> ruta = rutaRepository.findById(id_ruta);
        if (ruta.isEmpty())
            throw new EntityNotFoundException("No se ha encontrado la ruta buscada...");
        List<RutaEntity> rutas = estacion.get().getRutas();
        return rutas;
    }
}
