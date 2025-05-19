package es.uned.lsi.eped.pract2024_2025;

import es.uned.lsi.eped.DataStructures.BSTree;
import es.uned.lsi.eped.DataStructures.BSTreeIF;
import es.uned.lsi.eped.DataStructures.IteratorIF;

public class TaskPlannerTree implements TaskPlannerIF{

	/* Declaración de atributos para almacenar la información del planificador de tareas */
	protected BSTreeIF<TaskIF> futureTasks;
	protected BSTreeIF<TaskIF> pastTasks;

	/* Estructura que almacena las tareas pasadas */
	/* La estructura que almacena las tareas futuras debe ser un BSTree */
	public TaskPlannerTree() {
		this.futureTasks = new BSTree<>(BSTreeIF.Order.ASCENDING);
		this.pastTasks   = new BSTree<>(BSTreeIF.Order.ASCENDING);
	}

	/* Añade una nueva tarea
	 * @param text: descripción de la tarea
	 * @param date: fecha en la que la tarea debe completarse
	 */
	@Override
	public void add(String text, int date) {
		TaskIF task = new Task(text, date);
		futureTasks.add(task);
	}


	/* Elimina una tarea
	 * @param date: fecha de la tarea que se debe eliminar
	 */
	@Override
	public void delete(int date) {
		IteratorIF<TaskIF> it = futureTasks.iterator(BSTreeIF.IteratorModes.DIRECTORDER);
		while (it.hasNext()) {
			TaskIF curr = it.getNext();
			if (curr.getDate() == date) {
				futureTasks.remove(curr);
				return;
			}
		}
	}


	/* Reprograma una tarea
	 * @param origDate: fecha actual de la tarea
	 * @param newDate: nueva fecha de la tarea
	 */
	@Override
	public void move(int origDate, int newDate) {
		IteratorIF<TaskIF> it = futureTasks.iterator(BSTreeIF.IteratorModes.DIRECTORDER);
		TaskIF toMove = null;
		while (it.hasNext()) {
			TaskIF curr = it.getNext();
			if (curr.getDate() == origDate) {
				toMove = curr;
				break;
			}
		}
		if (toMove != null) {
			futureTasks.remove(toMove);
			futureTasks.add(new Task(toMove.getText(), newDate));
		}
	}


	/* Ejecuta la próxima tarea:
	 * la mete en el histórico marcándola como completada
	 */
	@Override
	public void execute() {
		IteratorIF<TaskIF> it = futureTasks.iterator(BSTreeIF.IteratorModes.DIRECTORDER);
		if (it.hasNext()) {
			TaskIF task = it.getNext();
			task.setCompleted();
			futureTasks.remove(task);
			pastTasks.add(task);
		}
	}


	/* Descarta la próxima tarea:
	 * la mete en el histórico marcándola como no completada
	 */
	@Override
	public void discard() {
		IteratorIF<TaskIF> it = futureTasks.iterator(BSTreeIF.IteratorModes.DIRECTORDER);
		if (it.hasNext()) {
			TaskIF task = it.getNext();
			futureTasks.remove(task);
			pastTasks.add(task);
		}
	}

	/* Devuelve un iterador de las tareas futuras */
	@Override
	public IteratorIF<TaskIF> iteratorFuture() {
		return futureTasks.iterator(BSTreeIF.IteratorModes.DIRECTORDER);
	}

	/* Devuelve un iterador del histórico de tareas pasadas */
	@Override
	public IteratorIF<TaskIF> iteratorPast() {
		return pastTasks.iterator(BSTreeIF.IteratorModes.DIRECTORDER);
	}
		
}
