package com.ArgusAPI.service;

import com.ArgusAPI.domain.model.Condominio;
import com.ArgusAPI.domain.model.Usuarios;
import com.ArgusAPI.dto.UsuarioDTO;
import com.ArgusAPI.exception.CondominioNotFoundException;
import com.ArgusAPI.repository.CondominioRepository;
import com.ArgusAPI.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UsuarioService {
    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private CondominioRepository condominioRepository;

    public Usuarios createUser(Usuarios usuarios) {
        if (usuarios.getCondominio() != null && usuarios.getCondominio().getNome() != null) {
            Condominio condominio = condominioRepository.findByNome(usuarios.getCondominio().getNome())
                    .orElseThrow(() -> new CondominioNotFoundException("Condomínio não encontrado"));
            usuarios.setCondominio(condominio);
        }

        return usuarioRepository.save(usuarios);
    }


    public List<UsuarioDTO> getAllUsers() {
        return usuarioRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public Optional<UsuarioDTO> findUserById(Long id) {
        return usuarioRepository.findById(id).map(this::convertToDTO);
    }

    public Usuarios updateUser(Long id, Usuarios usuarios) throws Exception {
        Usuarios existingUser = usuarioRepository.findById(id)
                .orElseThrow(() -> new Exception("Usuário não encontrado"));

        existingUser.setNome(usuarios.getNome());
        existingUser.setCpf(usuarios.getCpf());
        existingUser.setSenha(usuarios.getSenha());
        existingUser.setTelefone(usuarios.getTelefone());
        existingUser.setTipoDoUsuario(usuarios.getTipoDoUsuario());
        existingUser.setBloco(usuarios.getBloco());
        existingUser.setApartamento(usuarios.getApartamento());

        if (usuarios.getCondominio() != null && usuarios.getCondominio().getNome() != null) {
            Condominio condominio = condominioRepository.findByNome(usuarios.getCondominio().getNome())
                    .orElseThrow(() -> new CondominioNotFoundException("Condomínio não encontrado"));
            existingUser.setCondominio(condominio);
        }

        return usuarioRepository.save(existingUser);
    }


    public UsuarioDTO deleteUser(Long id) throws Exception {
        Usuarios usuarios = usuarioRepository.findById(id)
                .orElseThrow(() -> new Exception("Usuário não encontrado"));

        usuarioRepository.delete(usuarios);

        return convertToDTO(usuarios);
    }


    // Método auxiliar para converter User em UserDTO
    public UsuarioDTO convertToDTO(Usuarios usuarios) {
        return new UsuarioDTO(
                usuarios.getId(),
                usuarios.getNome(),
                usuarios.getTelefone(),
                usuarios.getTipoDoUsuario().name(),
                usuarios.getBloco(),
                usuarios.getApartamento(),
                usuarios.getCondominio() != null ? usuarios.getCondominio().getNome() : null,
                usuarios.getCondominio() != null ? usuarios.getCondominio().getEndereco() : null
        );
    }
}
