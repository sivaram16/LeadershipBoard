import com.example.leadershipboard.ViewCoursesQuery

object DataStore {
    var studentList: List<ViewCoursesQuery.Student> = ArrayList<ViewCoursesQuery.Student>()
    fun setStudentListFromExternal(item: List<ViewCoursesQuery.Student>) {
        studentList = item;
    }

}