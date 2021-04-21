import sbt.{Def, _}

object Aliases {
  val customAliases: Seq[Def.Setting[State => State]] = {
    addCommandAlias("runAllTests", "dockerComposeUp; test; dockerComposeStop") ++
      addCommandAlias("redeployApp", "dockerComposeStop; dockerComposeUp")
  }
}
