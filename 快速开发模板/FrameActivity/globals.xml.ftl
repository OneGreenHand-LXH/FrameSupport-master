<?xml version="1.0"?>
<globals>
    <global id="hasNoActionBar" type="boolean" value="false" />
    <global id="parentActivityClass" value="" />
    <#if selectViewType == "activity">
        <global id="simpleLayoutName" value="${layoutName}" />
    <#elseif selectViewType == "fragment">
    	<global id="simpleLayoutName" value="${layoutFragmentName}" />
    </#if>
    <global id="excludeMenu" type="boolean" value="true" />
    <global id="generateActivityTitle" type="boolean" value="false" />
    <#include "../common/common_globals.xml.ftl" />
</globals>