package com.example.smartlamp.repository

//
//class UserRepository @Inject constructor(
//    private val service: UserService,
//    private val dispatcher: CoroutineDispatcher,
//) {
//
//    private var users: MutableList<User> = mutableListOf()
//    private var user: User = User("","","",-1,"","")
//
//    suspend fun getListUser(): List<User> = withContext(dispatcher) {
//        val savedUser = service.getAllUsers()
//        users = savedUser.toMutableList()
//        users
//    }
//
//    suspend fun getUser(email: String): User = withContext(dispatcher) {
//        val savedUser = service.getUser(email)
//        user = savedUser
//        user
//    }
//
//    suspend fun saveNewUser(add: User) =
//        withContext(dispatcher) {
//            service.saveUser(add)
//        }
//
//    suspend fun deleteUser(user: User) = withContext(dispatcher) {
//        service.deleteUser(user)
//    }
//
//    suspend fun deleteAll() = withContext(dispatcher) {
//        service.deleteAll()
//    }
//
//}