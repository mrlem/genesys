package domain.repository

import domain.model.RootPolicy
import domain.model.Tree
import java.io.IOException

interface GenealogyRepository {

    @Throws(NoSuchElementException::class, IOException::class)
    fun getTree(filename: String, rootPolicy: RootPolicy): Tree
}