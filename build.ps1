$versionPath=$PSScriptRoot+"/version.properties"
$versionXml=([xml](Get-Content $versionPath))
$versionProperty=$versionXml
$ReleaseVersion=$versionProperty.VERSION_NAME+"."+$versionProperty.VERSION_CODE
$ReleaseVersion
Add-Content -Path $env:GITHUB_ENV -Value "ReleaseVersion=${ReleaseVersion}"