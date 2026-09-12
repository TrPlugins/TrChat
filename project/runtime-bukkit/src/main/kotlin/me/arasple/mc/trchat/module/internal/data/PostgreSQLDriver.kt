package me.arasple.mc.trchat.module.internal.data

import taboolib.common.env.RuntimeDependencies
import taboolib.common.env.RuntimeDependency

@RuntimeDependencies(
    RuntimeDependency(
        "!org.postgresql:postgresql:42.7.9",
        test = "!org.postgresql_42_7_9.Driver",
        relocate = ["!org.postgresql", "!org.postgresql_42_7_9"],
        transitive = false
    )
)
object PostgreSQLDriver