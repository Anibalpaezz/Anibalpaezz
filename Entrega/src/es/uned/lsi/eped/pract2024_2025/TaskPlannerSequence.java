package es.uned.lsi.eped.pract2024_2025;

import es.uned.lsi.eped.DataStructures.SequenceIF;
import es.uned.lsi.eped.DataStructures.List;
import es.uned.lsi.eped.DataStructures.ListIF;
import es.uned.lsi.eped.DataStructures.IteratorIF;

public class TaskPlannerSequence implements TaskPlannerIF{

	/* Declaración de atributos para almacenar la información del planificador de tareas */
	private final SequenceIF<TaskIF> futureTasks;
	private final SequenceIF<TaskIF> pastTasks;

	/* Estructura que almacena las tareas pasadas */
	/* La estructura que almacena las tareas futuras debe ser una secuencia */
	public TaskPlannerSequence() {
		this.futureTasks = new List<>();
		this.pastTasks   = new List<>();
	}

	/* Añade una nueva tarea
	 * @param text: descripción de la tarea
	 * @param date: fecha en la que la tarea debe completarse
	 */
	@Override
	public void add(String text, int date) {
		TaskIF task = new Task(text, date);
		int pos = 1;
		IteratorIF<TaskIF> it = futureTasks.iterator();
		while (it.hasNext()) {
			TaskIF curr = it.getNext();
			if (curr.getDate() > date) {
				break;
			}
			pos++;
		}
		((ListIF<TaskIF>) futureTasks).insert(pos, task);
	}


	/* Elimina una tarea
	 * @param date: fecha de la tarea que se debe eliminar
	 */
	@Override
	public void delete(int date) {
		IteratorIF<TaskIF> it = futureTasks.iterator();
		int pos = 1;
		while (it.hasNext()) {
			TaskIF curr = it.getNext();
			if (curr.getDate() == date) {
				((ListIF<TaskIF>) futureTasks).remove(pos);
				return;
			}
			pos++;
		}
	}


	/* Reprograma una tarea
	 * @param origDate: fecha actual de la tarea
	 * @param newDate: nueva fecha de la tarea
	 */
	@Override
	public void move(int origDate, int newDate) {
		IteratorIF<TaskIF> it = futureTasks.iterator();
		int pos = 1;
		TaskIF toMove = null;
		int foundPos = -1;
		while (it.hasNext()) {
			TaskIF curr = it.getNext();
			if (curr.getDate() == origDate) {
				toMove = curr;
				foundPos = pos;
				break;
			}
			pos++;
		}
		if (toMove != null) {
			((ListIF<TaskIF>) futureTasks).remove(foundPos);
			add(toMove.getText(), newDate);
		}
	}


	/* Ejecuta la próxima tarea:
	 * la mete en el histórico marcándola como completada
	 */
	@Override
	public void execute() {
		if (!futureTasks.isEmpty()) {
			ListIF<TaskIF> list = (ListIF<TaskIF>) futureTasks;
			TaskIF task = list.get(1);
			task.setCompleted();
			list.remove(1);
			((ListIF<TaskIF>) pastTasks).insert(pastTasks.size() + 1, task);
		}
	}


	/* Descarta la próxima tarea:
	 * la mete en el histórico marcándola como no completada
	 */
	@Override
	public void discard() {
		if (!futureTasks.isEmpty()) {
			ListIF<TaskIF> list = (ListIF<TaskIF>) futureTasks;
			TaskIF task = list.get(1);
			list.remove(1);
			((ListIF<TaskIF>) pastTasks).insert(pastTasks.size() + 1, task);
		}
	}

	/* Devuelve un iterador de las tareas futuras */
	@Override
	public IteratorIF<TaskIF> iteratorFuture() {
		return futureTasks.iterator();
	}

	/* Devuelve un iterador del histórico de tareas pasadas */
	@Override
	public IteratorIF<TaskIF> iteratorPast() {
		return pastTasks.iterator();
	}
		
}
