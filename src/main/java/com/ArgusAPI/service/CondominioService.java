package com.ArgusAPI.service;

import com.ArgusAPI.domain.model.Condominio;
import com.ArgusAPI.dto.CondominioDTO;
import com.ArgusAPI.repository.CondominioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CondominioService {
    @Autowired
    private CondominioRepository condominioRepository;

    public Condominio createCondominio(CondominioDTO condominioDTO) {
        Condominio condominio = new Condominio();
        condominio.setNome(condominioDTO.nome());
        condominio.setEndereco(condominioDTO.endereco());
        return condominioRepository.save(condominio);
    }

    public List<Condominio> getAllCondominios() {
        return condominioRepository.findAll();
    }

    public Condominio getCondominioById(Long id) throws Exception {
        return condominioRepository.findById(id).orElse(null);
    }

    public Condominio updateCondominio(Long id, CondominioDTO condominioDTO) {
        Condominio condominio = condominioRepository.findById(id).orElse(null);
        if (condominio != null) {
            condominio.setNome(condominioDTO.nome());
            condominio.setEndereco(condominioDTO.endereco());
            return condominioRepository.save(condominio);
        }
        return null;
    }

    public void deleteCondominio(Long id) {
        condominioRepository.deleteById(id);
    }
}
