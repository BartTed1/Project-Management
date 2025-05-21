package xyz.teodorowicz.pm.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.stereotype.Repository
import xyz.teodorowicz.pm.entity.Team;
interface TeamRepository : JpaRepository<Team, String>, JpaSpecificationExecutor<Team>