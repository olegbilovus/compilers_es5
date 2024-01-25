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

// PROTOTYPES
void main();
F_return fib(int n);
bool isEven(int n);

// PROCEDURES
void main(){
int n;

printf("%s\n", "Enter an integer number > 1");
scanf("%d", &n);
stdin_flush();

if(n < 1){
printf("%d%s\n", n, " is not a valid number");
}else {
 int res;

bool even;

// assign multi return function
{
F_return $r = fib(n);
res = *(int *)$r.val;
even = *(bool *)$r.next->val;
}

printf("%s%d\n", "Result: ", res);
printf("%s", "The number is ");
if(!even){
printf("%s", "not ");
}
printf("%s\n", "even.");
}
}


// FUNCTIONS
F_return fib(int n){
int res;
int res1;
int res2;

bool even;
bool even1;
bool even2;

if(n == 0 || n == 1){
// return
{
int $t0 = 1 ;
F_return $r0 = { &$t0 };
bool $t1 = false ;
F_return $r1 = { &$t1 };
return f_return((F_return[]){$r0, $r1}, 2);
}

}
// assign multi return function
{
F_return $r = fib(n - 1);
res1 = *(int *)$r.val;
even1 = *(bool *)$r.next->val;
}
// assign multi return function
{
F_return $r = fib(n - 2);
res2 = *(int *)$r.val;
even2 = *(bool *)$r.next->val;
}

res = res1 + res2;

even = isEven(res);

// return
{
int $t0 = res ;
F_return $r0 = { &$t0 };
bool $t1 = even ;
F_return $r1 = { &$t1 };
return f_return((F_return[]){$r0, $r1}, 2);
}

}

bool isEven(int n){
bool isEven = false;

int i = 1;

while(i < n){
isEven = !isEven;

i = i + 1;

}
return isEven;
}

