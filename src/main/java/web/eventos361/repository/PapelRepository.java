package web.eventos361.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import web.eventos361.model.Papel;

public interface PapelRepository extends JpaRepository<Papel, Long> {
}