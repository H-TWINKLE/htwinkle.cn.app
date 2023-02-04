$versionPath=$PSScriptRoot+"/version.properties"
$versionProperty = convertfrom-stringdata (get-content $versionPath -raw)
$ReleaseVersion=$versionProperty.VERSION_NAME+"."+$versionProperty.VERSION_CODE
$ReleaseVersion
Add-Content -Path $env:GITHUB_ENV -Value "ReleaseVersion=${ReleaseVersion}"