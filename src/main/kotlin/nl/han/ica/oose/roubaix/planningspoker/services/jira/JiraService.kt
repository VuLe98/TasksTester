package nl.han.ica.oose.roubaix.planningspoker.services.jira

class JiraService {
    fun editJiraTask(taskCode: String, estimate: Int, playerToken: String) {
        println("I am edited in JIRA! $taskCode and $estimate as $playerToken")
    }
}
