# horarios
A desktop app for managing class schedules

## Usage

![Select schedule](sample-images/start%20screen.jpg)

In the starting page, press "Go" to start to generate the schedule, or "Import" to see a previously exported configuration.
Every desired class can be added by using the checkboxes that appear in the next window.

![Select schedule](sample-images/select%20schedule.jpg)

Type a course name, a subject name, select the corresponding checkboxes and click the "Añadir curso" button to create the course.
You can add more courses to the subject and they will be reflected in the side window, showing a tree of added subjects and courses.
When every course for that subject has been added, click "Siguiente" to add a new subject or "Finalizar" to start generating the possible courses.
Clicking "Finalizar" will generate a table similar to the shown below:

![Select schedule](sample-images/show%20schedule.jpg)

If a course has been closed after you have generated the schedule, you can disable it in the sidenav by clicking the "Disable" button.
Disabled courses will be shown in a gray font color, and if every course of a subject is disabled, that subject won't be taken into account while creating the schedule.

![Select schedule](sample-images/disabling%20courses.jpg)

Note that the software will try to fit every enabled subject into the schedule, so alternative elective courses should be created as the same subject.

Clicking the "Exportar" button will allow you to export all added courses for easy continuing your schedule planification later.
