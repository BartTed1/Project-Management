package xyz.teodorowicz.pm.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.stereotype.Repository
import xyz.teodorowicz.pm.entity.Project

@Repository
interface ProjectRepository : JpaRepository<Project, String>, JpaSpecificationExecutor<Project>
