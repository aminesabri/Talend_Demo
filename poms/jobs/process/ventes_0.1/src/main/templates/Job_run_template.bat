%~d0
cd %~dp0
java -Dtalend.component.manager.m2.repository=%cd%/../lib -Xms256M -Xmx1024M -Dfile.encoding=UTF-8 -cp .;../lib/routines.jar;../lib/advancedPersistentLookupLib-1.2.jar;../lib/antlr-runtime-3.5.2.jar;../lib/commons-collections-3.2.2.jar;../lib/commons-collections4-4.1.jar;../lib/dom4j-1.6.1.jar;../lib/geronimo-stax-api_1.0_spec-1.0.1.jar;../lib/jboss-serialization.jar;../lib/log4j-1.2.17.jar;../lib/org.talend.dataquality.parser.jar;../lib/poi-3.16-20170419_modified_talend.jar;../lib/poi-ooxml-3.16-20170419_modified_talend.jar;../lib/poi-ooxml-schemas-3.16-20170419.jar;../lib/poi-scratchpad-3.16-20170419.jar;../lib/talend_file_enhanced_20070724.jar;../lib/talendcsv.jar;../lib/trove.jar;../lib/xmlbeans-2.6.0.jar;ventes_0_1.jar; demotalend.ventes_0_1.Ventes  %*