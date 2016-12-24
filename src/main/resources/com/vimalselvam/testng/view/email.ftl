<#assign systemAttributeContext=report.getSystemAttributeContext().getSystemAttributeList()>
<#assign pass="#8CA93E">
<#assign fail="#FF7F7F">

<!doctype html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Emailable Report</title>
</head>
<body>
<h1>Emailable Report</h1>

<#list systemAttributeContext>
<p>
    <em>System Configuration</em><br />
    <#items as sysConfig>
        <strong>${sysConfig.name}</strong>: ${sysConfig.value}<#sep>,
    </#items>
</p>
</#list>

<#list report.testList>
<table width="100%" border="1" cellspacing="2" cellpadding="10" style="border-collapse: collapse; display: table;">
    <tbody>
        <tr>
          <th>Test Method</th>
          <th>Status</th>
          <th>Start Time</th>
          <th>Duration</th>
        </tr>

        <#items as parent>
          <#list parent.nodeContext.all>
            <#items as child>
              <tr>
                <th colspan="4">Test Name: <em>${child.name}</em> (Suite: <em>${parent.name}</em>)</th>
              </tr>

              <#list child.nodeContext.all>
                <#items as grandchild>
                  <tr bgcolor=<#if grandchild.status=="pass">${pass}<#else>${fail}</#if>>
                    <td>
                        <font color="white"><em>${grandchild.name}</em></font>
                    </td>
                    <td>
                        <font color="white">${grandchild.status?upper_case}</font>
                    </td>
                    <td>
                        <font color="white">${grandchild.startTime?datetime?string}</font>
                    </td>
                    <td>
                        <font color="white">${grandchild.getRunDuration()?string}</font>
                    </td>
                  </tr>
                </#items>
              </#list>
            </#items>
          </#list>
        </#items>
    </tbody>
</table>
</#list>

<p style="color: gray; font-size: 10px; text-align: right;">
    Reports by <a href="http://vimalselvam.com">vimalselvam.com</a>
</p>
</body>
</html>