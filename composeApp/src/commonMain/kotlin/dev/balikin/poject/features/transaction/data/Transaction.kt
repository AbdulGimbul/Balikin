package dev.balikin.poject.features.transaction.data

data class Transaction(
    val name: String,
    val date: String,
    val note: String,
    val amount: String,
    val type: String
)

val dummyTransactions = listOf(
    Transaction(
        name = "Ilham Ardiansyah",
        date = "17 Agustus ● 12.22 PM",
        note = "bekas bell baras Ng",
        amount = "Rp. 459.000",
        type = "Utang"
    ),
    Transaction(
        name = "Irsan Ramadhan",
        date = "17 Agustus ● 12.22 PM",
        note = "bekas bell baras Ng",
        amount = "Rp. 99.000",
        type = "Piutang"
    ),
    Transaction(
        name = "Winggar Waharjut",
        date = "17 Agustus ● 12.22 PM",
        note = "bekas bell baras Ng",
        amount = "Rp. 43.000",
        type = "Piutang"
    )
)