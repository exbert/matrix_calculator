package processor

class Matrix(var row: Int, var column: Int) {
    var data = MutableList(row) { MutableList(column) { 0.0 } }

}

class MatrixD(var row: Int = 1, var column: Int = 1) {
    var data = MutableList(row) { MutableList(column) { 0.0 } }

}

var matrixList = mutableListOf<MatrixD>()
var matrixSizeInfo = mutableListOf<Int>() // 1r 1c 2r 2c
var matrixInverse = MatrixD()
var N = 0

fun main() {

    //createMatrix()
    //createMatrix()
    //printMatrixList()
    //var (tempMatrix, checkpoint )= sumMatrixes()
    //if (checkpoint == true) printSingleMatrix(tempMatrix)
    //printMatrixList()
    //multiplyMatrix()
    displayMenu()

}

fun displayMenu() {
    var checkpoint = false

    while (!checkpoint) {
        println("1. Add matrices")
        println("2. Multiply matrix by a constant")
        println("3. Multiply matrices")
        println("4. Transpose matrix")
        println("5. Calculate a determinant")
        println("6. Inverse matrix")
        println("0. Exit")
        print("Your choice: ")
        var input = readln().toInt()

        when (input) {
            1 -> {
                createTwoMatrix()
                var (tempMatrix, tempCheck) = sumMatrixes()
                if (tempCheck == true) printSingleMatrix(tempMatrix)
            }
            2 -> {
                multiplyMatrixByCons(0.0)
            }
            3 -> {
                createTwoMatrix()
                multiplyTwoMatrix()

            }
            4 -> {
                var outputT = chooseTranspose(0, matrixInverse)
                printSingleMatrixD(outputT)
            }
            5 -> {
                var matriksNew = createSingleMatrix()
                var result = calcDeterminant(matriksNew, matriksNew.row)
                println("The result is:")
                println(result)
            }
            6 -> {
                var matriksNew = createSingleMatrix()
                N = matriksNew.row
                var matrixAdj = MatrixD(N,N)
                var matrixInv = MatrixD(N,N)
                if (inverseMatrix(matriksNew, matrixInv)) display(matrixInv)
            }
            0 -> checkpoint = true
        }
    }
}

fun inverseMatrix(A: MatrixD, inverse: MatrixD): Boolean {
    // Find determinant of A[][]
    val det = determinant(A, N)
    if (det == 0.0) {
        print("This matrix doesn't have an inverse.")
        return false
    }

    // Find adjoint
    val adj = MatrixD(N,N)
    adjoint(A, adj)

    // Find Inverse using formula "inverse(A) = adj(A)/det(A)"
    for (i in 0 until N) for (j in 0 until N) inverse.data[i][j] = adj.data[i][j] / det
    return true
}

fun calcDeterminant(matriksNew: MatrixD, n: Int): Double {
    var result = 1.0
    var num1: Double
    var num2: Double
    var det = 1.0
    var index: Int
    var total = 1.0

    val temp = DoubleArray(n + 1)

    for (i in 0 until n) {
        index = i

        while (matriksNew.data[index][i] == 0.0 && index < n) {
            index++
        }

        if (index == n) continue

        if (index != i) {

            for (j in 0 until n) {
                swapPosition(matriksNew, index, j, i, j)
            }

            det = det * Math.pow(-1.0, (index - i).toDouble())
        }

        for (j in 0 until n) {
            temp[j] = matriksNew.data[i][j]
        }

        for (j in i + 1 until n) {
            num1 = temp[i]
            num2 = matriksNew.data[j][i]

            for (k in 0 until n) {

                matriksNew.data[j][k] = (num1 * matriksNew.data[j][k] - num2 * temp[k])
            }
            total = (total * num1)
        }

    }

    for (i in 0 until n) {
        det = (det * matriksNew.data[i][i])
    }

    return det / total
}

fun swapPosition(arr: MatrixD, i1: Int, j1: Int, i2: Int, j2: Int): MatrixD {
    val tempA = arr.data[i1][j1]
    arr.data[i1][j1] = arr.data[i2][j2]
    arr.data[i2][j2] = tempA

    return arr
}


fun chooseTranspose(input: Int, matriksNew: MatrixD): MatrixD {
    var output = MatrixD()

    if (input == 5) {

        output = doTranspose(1, matriksNew)

    } else {

        println("1. Main diagonal")
        println("2. Side diagonal")
        println("3. Vertical line")
        println("4. Horizontal line")
        print("Your choice: ")
        var inputT = readln().toInt()

        var matriksNew = createSingleMatrix()
        when (inputT) {
            1, 2, 3, 4 -> {

                output = doTranspose(inputT, matriksNew)

            }
            else -> println("Bye!")
        }
    }
    return output
}

fun doTranspose(input: Int, matriksNew: MatrixD): MatrixD {

    var matriksNewT = MatrixD(matriksNew.column, matriksNew.row)

    when (input) {
        1 -> {
            for (i in 0 until matriksNew.row) {
                for (j in 0 until matriksNew.column) {
                    matriksNewT.data[j][i] = matriksNew.data[i][j]
                }
            }
        }
        2 -> {
            for (i in 0 until matriksNew.row) {
                for (j in 0 until matriksNew.column) {
                    //matriksNewT.data[j][i] = matriksNew.data[i][j]
                    matriksNewT.data[matriksNew.row - j - 1][matriksNew.row - i - 1] = matriksNew.data[i][j]
                }
            }
        }
        3 -> {
            for (i in 0 until matriksNew.row) {
                for (j in 0 until matriksNew.column) {
                    matriksNewT.data[i][matriksNew.column - j - 1] = matriksNew.data[i][j]
                }
            }
        }
        4 -> {
            for (i in 0 until matriksNew.row) {
                for (j in 0 until matriksNew.column) {
                    matriksNewT.data[matriksNew.column - i - 1][j] = matriksNew.data[i][j]
                }
            }
        }
    }
    return matriksNewT
}

fun multiplyTwoMatrix() {

    if (matrixSizeInfo[1] != matrixSizeInfo[2]) {
        println("The operation cannot be performed.")
    } else {
        var matrixNew = MatrixD(matrixSizeInfo[0], matrixSizeInfo[3])


        for (i in 0 until matrixSizeInfo[0]) {
            for (j in 0 until matrixSizeInfo[3]) {
                for (k in 0 until matrixSizeInfo[1]) {
                    matrixNew.data[i][j] += matrixList[0].data[i][k] * matrixList[1].data[k][j]
                }
            }
        }
        printSingleMatrix(matrixNew)
    }
}


fun multiplyMatrixByCons(inputC: Double) {


    if (inputC == 0.0 ) {
        var matriksNew = createSingleMatrix()

        println("Enter constant: ")
        var inputCons = readln().toFloat()
        println("The result is:")

        for (a in 0 until matriksNew.row) {
            for (b in 0 until matriksNew.column) {
                print("${matriksNew.data[a][b] * inputCons} ")
            }
            println()
        }
        println()
    } else {
        println("The result is:")

        for (a in 0 until matrixInverse.row) {
            for (b in 0 until matrixInverse.column) {
                print("${matrixInverse.data[a][b] * inputC} ")
            }
            println()
        }
        println()
    }
}

fun printSingleMatrix(tempMatrix: MatrixD) {
    println("The result is:")
    for (a in 0 until tempMatrix.row) {
        for (b in 0 until tempMatrix.column) {
            print("${tempMatrix.data[a][b]} ")
        }
        println()
    }
    println()
}

fun printSingleMatrixD(tempMatrix: MatrixD) {
    println("The result is:")
    for (a in 0 until tempMatrix.row) {
        for (b in 0 until tempMatrix.column) {
            print("${tempMatrix.data[a][b]} ")
        }
        println()
    }
    println()
}

fun sumMatrixes(): Pair<MatrixD, Boolean> {
    var checkpoint = false
    var sumMatrix = MatrixD(matrixSizeInfo[0], matrixSizeInfo[1])
    if (matrixSizeInfo[0] == matrixSizeInfo[2] && matrixSizeInfo[1] == matrixSizeInfo[3]) {
        checkpoint = true

        for (x in 0 until matrixList.size) {
            for (a in 0 until matrixList[x].row) {
                for (b in 0 until matrixList[x].column) {
                    sumMatrix.data[a][b] += matrixList[x].data[a][b]
                    //print("${matrixList[x].data[a][b]} ")
                }
            }
        }
    } else {
        println("The operation cannot be performed.")
    }
    return Pair(sumMatrix, checkpoint)
}

fun printMatrixList() {

    for (x in 0 until matrixList.size) {
        for (a in 0 until matrixList[x].row) {
            for (b in 0 until matrixList[x].column) {
                print("${matrixList[x].data[a][b]} ")
            }
            println()
        }
    }
}

fun createSingleMatrix(): MatrixD {
    matrixList.clear()

    print("Enter size of matrix: ")
    val input = readln().split(" ").map { it.toInt() }

    val rowG = input[0]
    matrixSizeInfo.add(rowG)
    val columnG = input[1]
    matrixSizeInfo.add(columnG)

    val matriksNew = MatrixD(rowG, columnG)

    println("Enter matrix: ")
    for (r in 0 until rowG) {
        val inpurRow = readln().split(" ").map { it.toDouble() }
        for (c in 0 until columnG) {
            matriksNew.data[r][c] = inpurRow[c]
        }
    }
    matrixList.add(matriksNew)
    return matriksNew
}

fun createTwoMatrix() {
    val matrixStackName = listOf("first", "second")

    matrixList.clear()
    matrixSizeInfo.clear()

    for (i in matrixStackName) {

        print("Enter size of $i matrix: ")
        val input = readln().split(" ").map { it.toInt() }

        val rowG = input[0]
        matrixSizeInfo.add(rowG)
        val columnG = input[1]
        matrixSizeInfo.add(columnG)

        val matriksNew = MatrixD(rowG, columnG)

        println("Enter $i matrix: ")
        for (r in 0 until rowG) {
            val inpurRow = readln().split(" ").map { it.toDouble() }
            for (c in 0 until columnG) {
                matriksNew.data[r][c] = inpurRow[c]
            }
        }

        matrixList.add(matriksNew)
    }
}

fun getCofactor(A: MatrixD, temp: MatrixD, p: Int, q: Int, n: Int) {
    var i = 0
    var j = 0

    // Looping for each element of the matrix
    for (row in 0 until n) {
        for (col in 0 until n) {
            // Copying into temporary matrix only those element
            // which are not in given row and column
            if (row != p && col != q) {
                temp.data[i][j++] = A.data[row][col]

                // Row is filled, so increase row index and
                // reset col index
                if (j == n - 1) {
                    j = 0
                    i++
                }
            }
        }
    }
}

/* Recursive function for finding determinant of matrix.
n is current dimension of A[][]. */
fun determinant(A: MatrixD, n: Int): Double {
    var D = 0.0 // Initialize result

    // Base case : if matrix contains single element
    if (n == 1) return A.data[0][0]
    val temp = MatrixD(N,N) // To store cofactors
    var sign = 1.0 // To store sign multiplier

    // Iterate for each element of first row
    for (f in 0 until n) {
        // Getting Cofactor of A[0][f]
        getCofactor(A, temp, 0, f, n)
        D += sign * A.data[0][f] * determinant(temp, n - 1)

        // terms are to be added with alternate sign
        sign = -sign
    }
    return D
}

// Function to get adjoint of A[N][N] in adj[N][N].
fun adjoint(A: MatrixD, adj: MatrixD) {
    if (N == 1) {
        adj.data[0][0] = 1.0
        return
    }

    // temp is used to store cofactors of A[][]
    var sign = 1.0
    val temp = MatrixD(N,N)
    for (i in 0 until N) {
        for (j in 0 until N) {
            // Get cofactor of A[i][j]
            getCofactor(A, temp, i, j, N)

            // sign of adj[j][i] positive if sum of row
            // and column indexes is even.
            sign = if ((i + j) % 2 == 0) 1.0 else -1.0

            // Interchanging rows and columns to get the
            // transpose of the cofactor matrix
            adj.data[j][i] = sign * determinant(temp, N - 1)
        }
    }
}

// Function to calculate and store inverse, returns false if
// matrix is singular
fun inverse(A: MatrixD, inverse: Array<DoubleArray>): Boolean {
    // Find determinant of A[][]
    val det = determinant(A, N)
    if (det == 0.0) {
        print("Singular matrix, can't find its inverse")
        return false
    }

    // Find adjoint
    val adj = MatrixD(N, N)
    adjoint(A, adj)

    // Find Inverse using formula "inverse(A) = adj(A)/det(A)"
    for (i in 0 until N) for (j in 0 until N) inverse[i][j] = adj.data[i][j] / det
    return true
}

fun display(A: Array<IntArray>) {
    for (i in 0 until N) {
        for (j in 0 until N) print(A[i][j].toString() + " ")
        println()
    }
}

fun display(A: MatrixD) {
    println("The result is:")
    for (i in 0 until N) {
        for (j in 0 until N) System.out.printf("%.6f ", A.data[i][j])
        println()
    }
}
