
                                README

                   JGoodies Looks(tm) Version 1.1.2
                        

CONTENTS

   * Introduction
   * Distribution Contents
   * Binary and Source Code
   * Support


INTRODUCTION

     The JGoodies Looks help you improve the visual appearance 
     of your Swing based applications and applets. They have been
     optimized for readability, precise layout, and more generally,
     usability. Furthermore, they provide several extensions and 
     corrections to Sun's Windows L&F and Sun's Java L&F.

     It is recommended to download the JGoodies Looks Demo from 
     www.JGoodies.com/downloads/demos.html
     This demo shows how to make use of how to configure the Looks.
     You can find more sample applications that use the looks at 
     www.JGoodies.com/freeware/
     
     The JGoodies L&Fs require Java 1.3 or later. 
     


DISTRIBUTION CONTENTS

     The JGoodies Looks package comes as a ZIP archive that contains:
       o docs/api            - JavaDocs directory
       o docs/reference      - developer reference

       o src/share           - Shared sources
       o src/motif           - Motif sources and resources
       o src/plastic         - Plastic sources and resources
       o src/windows         - Windows sources and resources
       o src/1.4             - library sources for 1.4-only classes
       o src/icons           - PhotoShop icon files
       o src/examples/tiny   - sources for a tiny example
       o src/examples/demo   - sources for a demo frame
       
       o LICENSE.txt         - the BSD license agreement 
       o changes.txt         - the change history
       o quickstart.txt      - a quick introduction to the Looks
       o readme.txt          - this readme file
       o todo.txt            - the to do-list
       o build.xml           - ANT build file
       o default.properties  - ANT default build properties
       o looks-1.1.2.jar     - a JAR containing all JGoodies l&fs
       o looks-win-1.1.2.jar - a JAR containing the JGoodies Windows l&f
       o plastic-1.1.2.jar   - a JAR containing the JGoodies Plastic l&fs
            
       
       
BINARY AND SOURCE CODE

     The JGoodies Looks require Java 1.3 or later. The following classes
     are used for 1.4 or later, and cannot be compiled in 1.3 environments:
         com.jgoodies.plaf.ExtBasicArrowButtonHandler
         com.jgoodies.plaf.plastic.PlasticSpinnerUI
         com.jgoodies.plaf.windows.ExtWindowsSpinnerUI
     
     Anyway, I recommend to refer the looks-all.jar in your IDE. 
     If you want to shrink or obfuscate your deployed code you may need 
     to exclude the 1.4 classes from the shrink/obfuscation process.
          
     

SUPPORT 

     The Looks project is maintained and unofficially supported 
     at JavaDesktop.org, see http://forms.dev.java.net

     Customers of the JGoodies Looks Pro and Swing Suite get 
     prioritiy support and can contact us by mail and phone too.
		  

------------------------------------------------------------------------
Copyright (c)¸ 2003 JGoodies Karsten Lentzsch. All rights reserved.

Java and all Java-based trademarks and logos are trademarks or 
registered trademarks of Sun Microsystems, Inc. in the United States 
and other countries. 

Windows is a registered trademark of Microsoft Corporation in the
United States and other countries.

ClearLook is a trademark of JGoodies Karsten Lentzsch