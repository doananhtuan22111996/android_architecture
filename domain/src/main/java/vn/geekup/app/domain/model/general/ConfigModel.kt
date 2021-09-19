package vn.geekup.app.domain.model.general

data class ConfigModel(

  var isForceUpdate: Boolean,

  var latestVersion: Int

) : BaseModel()