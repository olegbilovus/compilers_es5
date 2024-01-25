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
int MAX = 10;

// PROTOTYPES
void main();
void printResult(int in, int i);

// PROCEDURES
void main(){
int in;

printf("%s\n", "Input a positive integer");
scanf("%d", &in);
stdin_flush();

if(in < 0){
printf("%s%d%s\n", "'", in, "' is not a positive integer");
}else {
 int i = 0;

while(i <= MAX){
printResult(in, i);
i = i + 1;

}
}
}

void printResult(int in, int i){
printf("%d%s%d%s%d\n", in, " * ", i, " = ", in * i);
}


// FUNCTIONS
