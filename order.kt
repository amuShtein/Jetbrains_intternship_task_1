import java.util.Scanner

const val NUMBER_OF_LETTERS = 26

val table = Array<Array<Byte>>(NUMBER_OF_LETTERS) { Array(NUMBER_OF_LETTERS) { 0 } };
val flags = Array<Byte>(NUMBER_OF_LETTERS) {0}

fun addOrder(s: String, table: Array<Array<Byte>>): Boolean {
    var ret = true
    for(i in 1 until s.length) {
        val b = s[i-1] - 'a'
        val l = s[i] - 'a'

        ret = addPair(b, l, table) && ret
    }

    return ret
}

fun addPair(b: Int, l: Int, table: Array<Array<Byte>>): Boolean {
    var ret = true

    if(table[b][l] >= 0) {
        table[b][l] = 1
        table[l][b] = -1
        flags[b] = 1
        flags[l] = 1
    } else {
        return false
    }

    for (j in 0 until NUMBER_OF_LETTERS) {
        if (table[b][j] < 0) {
            ret = addPair(j, l, table)
        }

        if (table[l][j] > 0) {
            ret = addPair(b, j, table) && ret
        }
    }

    return ret
}

fun cmp(a: Char, b: Char): Byte {
    return table[a - 'a'][b - 'a']
}

fun sort(array: Array<Char>) {
    for (i in 0 until array.size-1) {
        for (j in i+1 until array.size) {
            if(cmp(array[j], array[i]) > 0) {
                val tmp = array[i]
                array[i] = array[j]
                array[j] = tmp
            }
        }
    }

}

class Tree {
    var charArray = ""
    var nodes = mutableListOf<Tree>()

    fun add(s: String, pos: Int) :Boolean {
        if(pos == s.length) {
            return (charArray == "")
        }

        var i = 0
        val len = charArray.length
        while(i < len) {
            if (charArray[i] == s[pos].toLowerCase()) {
                break
            }

            i++
        }

        if (i < len-1) {
            return false
        }

        if (i == len) {
            charArray += s[pos].toLowerCase()
            nodes.add(Tree())
        }

        return (nodes[i]).add(s, pos+1)
    }

    fun loadTree(): Boolean{
        var ret = true
        if (charArray.length >= 2) {
            ret = addOrder(charArray, table)
        }

        for (e in nodes) {
            ret = e.loadTree() && ret
        }

        return ret
    }
}



fun main() {
    val root = Tree()
    var flag = true;
    val input = Scanner(System.`in`)
    val n = input.nextInt()

    for (i in 1..n) {
        val s = input.next()

        flag = root.add(s, 0) && flag
    }

    if (!flag) {
        println("Impossible")
        return
    }

    flag = root.loadTree()

    if (!flag) {
        println("Impossible")
        return
    }

    val array = Array<Char>(NUMBER_OF_LETTERS) { i -> 'a' + i }
    var arrayToSort = array.filter { c -> flags[c-'a'] > 0}.toTypedArray()
    val unusedArray = array.filter { c -> flags[c-'a'] < 1}.toTypedArray()
    sort(arrayToSort)

    arrayToSort += unusedArray

    for(x in arrayToSort) {
        print("$x ")
    }

    return
}