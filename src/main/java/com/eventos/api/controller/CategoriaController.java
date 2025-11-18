package com.eventos.api.controller;

import com.eventos.api.dto.CategoriaDTO;
import com.eventos.api.service.CategoriaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/categorias")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class CategoriaController {

    private final CategoriaService categoriaService;

    @PostMapping
    public ResponseEntity<CategoriaDTO> crearCategoria(@Valid @RequestBody CategoriaDTO categoriaDTO) {
        CategoriaDTO nuevaCategoria = categoriaService.crearCategoria(categoriaDTO);
        return new ResponseEntity<>(nuevaCategoria, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<CategoriaDTO>> obtenerTodasLasCategorias() {
        List<CategoriaDTO> categorias = categoriaService.obtenerTodasLasCategorias();
        return ResponseEntity.ok(categorias);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoriaDTO> obtenerCategoriaPorId(@PathVariable Long id) {
        CategoriaDTO categoria = categoriaService.obtenerCategoriaPorId(id);
        return ResponseEntity.ok(categoria);
    }

    @GetMapping("/nombre/{nombre}")
    public ResponseEntity<CategoriaDTO> obtenerCategoriaPorNombre(@PathVariable String nombre) {
        CategoriaDTO categoria = categoriaService.obtenerCategoriaPorNombre(nombre);
        return ResponseEntity.ok(categoria);
    }

    @GetMapping("/activas")
    public ResponseEntity<List<CategoriaDTO>> obtenerCategoriasActivas() {
        List<CategoriaDTO> categorias = categoriaService.obtenerCategoriasActivas();
        return ResponseEntity.ok(categorias);
    }

    @GetMapping("/buscar")
    public ResponseEntity<List<CategoriaDTO>> buscarCategoriasPorNombre(@RequestParam String nombre) {
        List<CategoriaDTO> categorias = categoriaService.buscarCategoriasPorNombre(nombre);
        return ResponseEntity.ok(categorias);
    }

    @GetMapping("/{id}/eventos/count")
    public ResponseEntity<Long> contarEventosPorCategoria(@PathVariable Long id) {
        Long cantidadEventos = categoriaService.contarEventosPorCategoria(id);
        return ResponseEntity.ok(cantidadEventos);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoriaDTO> actualizarCategoria(
            @PathVariable Long id,
            @Valid @RequestBody CategoriaDTO categoriaDTO) {
        CategoriaDTO categoriaActualizada = categoriaService.actualizarCategoria(id, categoriaDTO);
        return ResponseEntity.ok(categoriaActualizada);
    }

    @PatchMapping("/{id}/estado")
    public ResponseEntity<CategoriaDTO> activarDesactivarCategoria(
            @PathVariable Long id,
            @RequestParam Boolean activo) {
        CategoriaDTO categoriaActualizada = categoriaService.activarDesactivarCategoria(id, activo);
        return ResponseEntity.ok(categoriaActualizada);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarCategoria(@PathVariable Long id) {
        categoriaService.eliminarCategoria(id);
        return ResponseEntity.noContent().build();
    }
}