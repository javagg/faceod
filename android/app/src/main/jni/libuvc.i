%module uvc
%{
#include <libuvc/libuvc.h>
%}

%include "enumtypeunsafe.swg"  
/* Force the generated Java code to use the C enum values rather than making a JNI call */
%javaconst(1);

%include <libuvc/libuvc.h>

// int fact(int n);