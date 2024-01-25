#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <stdbool.h>

typedef struct F_return{
  void * val;
  struct F_return *next;
} F_return;


char * _strcat(char *s1, char *s2){
  char *res = malloc((strlen(s1) + strlen(s2) + 1) * sizeof(char));
  strcat(res, s1);
  strcat(res, s2);
  return res;
}

void _scanf_string(char **p){
  fflush(stdin);
  char c;
  char *str = malloc(1 * sizeof(char));
  int len = 0;
  for(; (c = getchar()) != '\n' && c != EOF; len++){
    str = realloc(str, sizeof(char) * len + 1);
    str[len] = c;
  }
  str = realloc(str, sizeof(char) * len);
  str[len] = '\0';
  *p = str;
}

void stdin_flush(){
  fflush(stdin);
  char c;
  while ((c = getchar()) != EOF && c != '\n');
}

F_return f_return(F_return returns[], int len){
  for(int i = 0; i < len - 1; i++){
    returns[i].next = &returns[i + 1];
  }
  return returns[0];
}


// VAR DECLS
int a = 3;
double aa = 5.6;
double c = 3.4;
char * repl_for = "hello";
char * s = "multiple\r\n  lines\r\n  string";
char * s4;
char * s2 = "string";
char * s3 = "hello";

// PROTOTYPES
void main();
void repl_struct(double a, char * * c, double * ref);
double f();
F_return f2();
F_return f3();

// PROCEDURES
void main(){
double b;
double bb;

bool bol;

char * in;

bb = 0;

// assign multi return function
{
F_return $r = f2();
b = *(double *)$r.val;
bol = *(bool *)$r.next->val;
in = (char *)$r.next->next->val;
}

printf("%lf%s%d%s%s\n", b, ", ", bol, ", ", in);
printf("%s\n", _strcat("Enter an ", "input:"));
_scanf_string(&in);
printf("%s\n", "and two numbers:");
scanf("%lf%lf", &b, &bb);
stdin_flush();

repl_struct(b + bb, &in, &b);
if(strcmp(in, "hello") == 0){
printf("%s\n", _strcat("hello again", "!!"));
}
}

void repl_struct(double a, char * * c, double * ref){
char * d = "hello";

bool b = true;

bool b1 = false;

*c = _strcat(_strcat(d, *c), "end");

printf("%s%s\n", _strcat("Result", " is: "), *c);
*c = "hello";

*ref = 5;

printf("%d%lf", b || b1, f());
}


// FUNCTIONS
double f(){
return 1.1;
}

F_return f2(){
// return
{
double $t0 = 1.1 ;
F_return $r0 = { &$t0 };
bool $t1 = true ;
F_return $r1 = { &$t1 };
char * $t2 = "hello" ;
F_return $r2 = { $t2 };
return f_return((F_return[]){$r0, $r1, $r2}, 3);
}

}

F_return f3(){
// return
{
double $t0 = 1.1 ;
F_return $r0 = { &$t0 };
bool $t1 = true ;
F_return $r1 = { &$t1 };
return f_return((F_return[]){$r0, $r1}, 2);
}

}

