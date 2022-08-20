<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:soapenc="http://schemas.xmlsoap.org/soap/encoding/">

    <xsl:key name="multiref-by-id" match="multiRef" use="@id" />

    <xsl:template match="/">
        <xsl:copy>
            <xsl:apply-templates select="@*|*" />
        </xsl:copy>
    </xsl:template>

    <xsl:template match="*[starts-with(@href, '#')]">
        <xsl:copy>
            <xsl:apply-templates select="@* |
         key('multiref-by-id', substring-after(@href, '#'))/@*[not(local-name()='id' or local-name()='type')] |
        key('multiref-by-id', substring-after(@href, '#'))/node()" />
        </xsl:copy>
    </xsl:template>

    <xsl:template match="@href[starts-with(., '#')] | multiRef[@id] | @soapenc:root" />

    <xsl:template match="@*|node()">
        <xsl:copy>
            <xsl:apply-templates select="@*|node()" />
        </xsl:copy>
    </xsl:template>
</xsl:stylesheet>
