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
int c = 1;

// PROTOTYPES
void sommac(int a, int d, double b, char * * size, double * result);
void main();
char * stampa(double a, double b, int c, char * taglia, double risultato);

// PROCEDURES
void sommac(int a, int d, double b, char * * size, double * result){
*result = a + b + c + d;

if(*result > 100){
char * valore = "grande";

*size = valore;

}else if(*result > 50){
char * valore = "media";

*size = valore;

}else {
 char * valore = "piccola";

*size = valore;

}
}

void main(){
int a = 1;
double b = 2.2;

int x = 3;

char * taglia;
char * ans1;

char * ans = "no";

double risultato = 0.0;

char * valore = "nok";

sommac(a, x, b, &taglia, &risultato);
valore = stampa(a, b, c, taglia, risultato);

printf("%s\n", "vuoi continuare? (si/no) - inserisci due volte la risposta");
_scanf_string(&ans);
_scanf_string(&ans1);

while(strcmp(ans, "si") == 0){
printf("%s\n", "inserisci un intero:");
scanf("%d", &a);
stdin_flush();

printf("%s\n", "inserisci un reale:");
scanf("%lf", &b);
stdin_flush();

sommac(a, x, b, &taglia, &risultato);
valore = stampa(a, b, c, taglia, risultato);

printf("%s\n", "vuoi continuare? (si/no):\t");
_scanf_string(&ans);

}
printf("\n");
printf("%s", "ciao");
}


// FUNCTIONS
char * stampa(double a, double b, int c, char * taglia, double risultato){
int i = 0;

while(i < 4){
printf("\n");
i = i + 1;

}
printf("%s%lf%s%lf%s%d%s%s\n", "la somma di ", a, " e ", b, " incrementata di ", c, " è ", taglia);
printf("%s%lf\n", " ed è pari a ", risultato);
return "ok";
}

