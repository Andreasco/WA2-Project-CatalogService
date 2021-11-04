package it.polito.wa2project.wa2projectcatalogservice.services


/**
 * Enum to derive the operation that was performed in the DataBase.
 */
enum class DbOperation(private val code: String) {
    READ("r"),
    CREATE("c"),
    UPDATE("u"),
    DELETE("d");

    fun code(): String {
        return this.code
    }

    companion object {
        fun forCode(code: String?): DbOperation? {
            val var1 = values()
            val var2 = var1.size
            for (var3 in 0 until var2) {
                val op = var1[var3]
                if (op.code().equals(code, ignoreCase = true)) {
                    return op
                }
            }
            return null
        }
    }
}