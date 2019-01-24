import com.example.leadershipboard.ViewCoursesQuery

object DataStore {
    var studentList: List<ViewCoursesQuery.Student> = ArrayList<ViewCoursesQuery.Student>()
    var tempBucket: MutableList<String> = ArrayList<String>()
    fun setStudentListFromExternal(item: List<ViewCoursesQuery.Student>) {
        studentList = item
    }
    fun isRegisterNoAlreadyUsed(regno: String) : Boolean {
        for(selected in tempBucket){
            if(selected.equals(regno)){
                return true
            }
        }
        return false
    }

}