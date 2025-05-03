package xyz.teodorowicz.pm.specification

import jakarta.persistence.criteria.CriteriaBuilder
import jakarta.persistence.criteria.CriteriaQuery
import org.springframework.data.jpa.domain.Specification
import jakarta.persistence.criteria.Predicate
import jakarta.persistence.criteria.Root
import xyz.teodorowicz.pm.entity.Project
import xyz.teodorowicz.pm.enumeration.project.ProjectPriority
import xyz.teodorowicz.pm.enumeration.project.ProjectStatus

class ProjectSpecification(
    private val search: String?,
    private val status: ProjectStatus?,
    private val priority: ProjectPriority?,
) : Specification<Project> {

    override fun toPredicate(
        root: Root<Project>,
        query: CriteriaQuery<*>?,
        criteriaBuilder: CriteriaBuilder
    ): Predicate? {
        val predicates = mutableListOf<Predicate>()

        if (!search.isNullOrBlank()) {
            val searchPattern = "%${search.lowercase()}%"
            predicates += criteriaBuilder.or(
                criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), searchPattern),
                criteriaBuilder.like(criteriaBuilder.lower(root.get("description")), searchPattern)
            )
        }

        if (status != null) {
            predicates += criteriaBuilder.equal(root.get<ProjectStatus>("status"), status)
        }

        if (priority != null) {
            predicates += criteriaBuilder.equal(root.get<ProjectPriority>("priority"), priority)
        }

        return criteriaBuilder.and(*predicates.toTypedArray())
    }
}